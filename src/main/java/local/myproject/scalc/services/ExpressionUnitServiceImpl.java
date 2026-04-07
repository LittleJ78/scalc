package local.myproject.scalc.services;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.ParametersForExpressions;
import local.myproject.scalc.domain.ParametrisedExpressions;
import local.myproject.scalc.persistent.dao.ExpressionUnitDao;
import local.myproject.scalc.persistent.dao.ParametrisedExpressionDao;
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

    private final ExpressionUnitDao expressionUnitDao;
    private final ParametrisedExpressionDao parametrisedExpressionDao;
    public static String[][] arrayOfParameters = {};
    private static Character counter = 'A';

    @Override
    public ExpressionUnit findById(int expressionUnitId) {
        return expressionUnitDao.findById(expressionUnitId).orElseThrow();
    }

    @Override
    public List<ExpressionUnit> findAllByProjectId(int projectId) {
        this.createArrayOfParameters(projectId);
        return expressionUnitDao.findAllByProjectId(projectId);
    }

    @Override
    public List<ExpressionUnit> createWatchList(int projectId) {
        return expressionUnitDao.findAllByProjectId(projectId).stream().filter(x -> x.isWatchList()).toList();
    }

    @Override
    public void save(ExpressionUnit expressionUnit) {
        if(checkDoubleExpression(expressionUnit)) {
            expressionUnit.setExpressionUnitName(this.setDefaultName(expressionUnit.getProject().getProjectId()));
            ExpressionUnit savedExpressionUnit = expressionUnitDao.save(expressionUnit);
            expressionUnit.setExpressionUnitId(savedExpressionUnit.getExpressionUnitId());
            this.createArrayOfParameters(expressionUnit.getProject().getProjectId());
            this.findAndSaveOrUpdateParameters(expressionUnit);
        }
    }

    @Override
    public void update(ExpressionUnit expressionUnit) {
            expressionUnitDao.update(expressionUnit);
            this.findAndSaveOrUpdateParameters(expressionUnit);
    }


    @Override
    public void deleteById(int expressionUnitId) {
        int projectId = expressionUnitDao.findById(expressionUnitId).orElseThrow().getProject().getProjectId();
        expressionUnitDao.deleteById(expressionUnitId);
        this.createArrayOfParameters(projectId);
    }


    /**
     * создает массив всех возможных параметров из списка выражений в виде массива [параметр][значение], где
     * параметр это имя выражения, а значение это результат вычисления этого выражения. массив записывается в
     * статический массив класса arrayOfParameters
     */
    private void createArrayOfParameters(int projectId) {
        List<ExpressionUnit> listOfExpressionUnit = expressionUnitDao.findAllByProjectId(projectId);
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
        while (expressionUnitDao.findAllNameByProjectId(projectId).stream().filter(x -> x.equals(counter + "_Value") || x.equals(String.valueOf(counter))).count() != 0){
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
        List<ExpressionUnit> listExpressions = expressionUnitDao.findAll();
        return listExpressions.stream().filter(x -> x.getDefaultExpression().equals(expressionUnit.getDefaultExpression())).count() == 0;
    }

    /**
     * обрабатывает выражение и если есть параметры, то извлекает их и сохраняет в базу
     * если производиться обновление, то удаляет старые параметы и записывает новые
     */
    private void findAndSaveOrUpdateParameters(ExpressionUnit expressionUnit){
        int projectId = expressionUnit.getProject().getProjectId();
        List<String> units = Arrays.stream(expressionUnit.getDefaultExpression().split(" ")).toList();
        ParametrisedExpressions parametrisedExpressions = parametrisedExpressionDao.findByExpressionUnitId(expressionUnit.getExpressionUnitId()).orElse(new ParametrisedExpressions());
        parametrisedExpressions.clearParameter();
        parametrisedExpressions.setExpressionUnit(expressionUnit);
        List<String> namesOfExpressionsInProject = expressionUnitDao.findAllNameByProjectId(projectId);
        for(String s : units) {
            if (!namesOfExpressionsInProject.stream().filter(x -> x.equals(s)).findFirst().orElse("").equals("")) {
                ParametersForExpressions parametersForExpressions = new ParametersForExpressions();
                parametersForExpressions.setParameter(s);
                parametersForExpressions.setParametrisedExpressions(parametrisedExpressions);
                parametrisedExpressions.addParameter(parametersForExpressions);
            }
        }
        if (!parametrisedExpressions.getParametersForExpressions().isEmpty()) {
            if (parametrisedExpressions.getParametrisedExpressionId() == 0) {
                parametrisedExpressionDao.save(parametrisedExpressions);
            } else {
                parametrisedExpressionDao.update(parametrisedExpressions);
            }
        } else if (parametrisedExpressions.getParametrisedExpressionId() != 0) {
            parametrisedExpressionDao.deleteById(parametrisedExpressions.getParametrisedExpressionId());
        }
    }
}
