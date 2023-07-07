package local.myproject.scalc.services;

import local.myproject.scalc.entitys.ExpressionUnit;
import local.myproject.scalc.entitys.ParametersForExpressions;
import local.myproject.scalc.entitys.ParametrisedExpressions;
import local.myproject.scalc.repositories.ExpressionUnitRepository;
import local.myproject.scalc.repositories.ParametrisedExpressionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExpressionUnitServiceImpl implements ExpressionUnitService{

    private final ExpressionUnitRepository expressionUnitRepository;
    private final ParametrisedExpressionsRepository parametrisedExpressionsRepository;
    public static String[][] arrayOfParameters = {};
    private static Character counter = 'A';

    @Override
    public ExpressionUnit findById(int expressionUnitId) {
        return expressionUnitRepository.findById(expressionUnitId).get();
    }

    @Override
    public List<ExpressionUnit> findAllByProjectId(int projectId) {
        this.createArrayOfParameters(projectId);
        return expressionUnitRepository.findAllByProjectId(projectId);
    }

    @Override
    public List<ExpressionUnit> createWatchList(int projectId) {
        return expressionUnitRepository.findAllByProjectId(projectId).stream().filter(x -> x.isWatchList()).toList();
    }

    @Override
    public void save(ExpressionUnit expressionUnit) {
        if(checkDoubleExpression(expressionUnit)) {
            expressionUnit.setExpressionUnitName(this.setDefaultName(expressionUnit.getProject().getProjectId()));
            expressionUnitRepository.save(expressionUnit);
            this.createArrayOfParameters(expressionUnit.getProject().getProjectId());
            this.findAndSaveOrUpdateParameters(expressionUnit);
        }
    }

    @Override
    public void update(ExpressionUnit expressionUnit) {
            expressionUnitRepository.save(expressionUnit);
            this.findAndSaveOrUpdateParameters(expressionUnit);
    }


    @Override
    public void deleteById(int expressionUnitId) {
        int projectId = expressionUnitRepository.findById(expressionUnitId).get().getProject().getProjectId();
        expressionUnitRepository.deleteById(expressionUnitId);
        this.createArrayOfParameters(projectId);
    }


    /**
     * создает массив всех возможных параметров из списка выражений в виде массива [параметр][значение], где
     * параметр это имя выражения, а значение это результат вычисления этого выражения. массив записывается в
     * статический массив класса arrayOfParameters
     */
    private void createArrayOfParameters(int projectId) {
        List<ExpressionUnit> listOfExpressionUnit = expressionUnitRepository.findAllByProjectId(projectId);
        String[][] parameter = new String[listOfExpressionUnit.size()][2];
        for(int i = 0; i < listOfExpressionUnit.size(); i++) {
            parameter[i][0] = listOfExpressionUnit.get(i).getExpressionUnitName();
            parameter[i][1] = listOfExpressionUnit.get(i).getExpressionResult();
        }
        arrayOfParameters = parameter;
    }

    /**
     * метод генерации имени выражения
     * @return имя выражения
     */
    private String setDefaultName(int projectId) {
        counter = 'A';
        while (expressionUnitRepository.findAllNameByProjectId(projectId).stream().filter(x -> x.equals(counter + "_Value") || x.equals(String.valueOf(counter))).count() != 0){
            counter++;
        }
        return String.valueOf(counter);
    }
    /**
     * метод проверки на дубликат арифметического выражения в базе
     * @param expressionUnit текущее выражение
     * @return правда если совпадений нет
     */
    private Boolean checkDoubleExpression(ExpressionUnit expressionUnit) {
        List<ExpressionUnit> listExpressions = expressionUnitRepository.findAll();
        return listExpressions.stream().filter(x -> x.getDefaultExpression().equals(expressionUnit.getDefaultExpression())).count() == 0;
    }

    /**
     * обрабатывает выражение и если есть параметры, то извлекает их и сохраняет в базу
     * если производиться обновление, то удаляет старые параметы и записывает новые
     */
    private void findAndSaveOrUpdateParameters(ExpressionUnit expressionUnit){
        int projectId = expressionUnit.getProject().getProjectId();
        List<String> units = Arrays.stream(expressionUnit.getDefaultExpression().split(" ")).toList();
        ParametrisedExpressions parametrisedExpressions = parametrisedExpressionsRepository.FindByExpressionUnitId(expressionUnit.getExpressionUnitId()).stream().findFirst().orElse(new ParametrisedExpressions());
        parametrisedExpressions.clearParameter();
        parametrisedExpressions.setExpressionUnit(expressionUnit);
        List<String> namesOfExpressionsInProject = expressionUnitRepository.findAllNameByProjectId(projectId);
        for(String s : units) {
            if (!namesOfExpressionsInProject.stream().filter(x -> x.equals(s)).findFirst().orElse("").equals("")) {
                ParametersForExpressions parametersForExpressions = new ParametersForExpressions();
                parametersForExpressions.setParameter(s);
                parametersForExpressions.setParametrisedExpressions(parametrisedExpressions);
                parametrisedExpressions.addParameter(parametersForExpressions);
            }
        }
        if (!parametrisedExpressions.getParametersForExpressions().isEmpty()) {
            parametrisedExpressionsRepository.save(parametrisedExpressions);
        } else if (parametrisedExpressions.getParametrisedExpressionId() != 0) {
            parametrisedExpressionsRepository.delete(parametrisedExpressions);
        }
    }
}
