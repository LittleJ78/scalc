package local.myproject.scalc.application.expression.usecase;

import local.myproject.scalc.application.expression.port.out.ExpressionParameterPort;
import local.myproject.scalc.application.expression.port.out.ExpressionUnitPort;
import local.myproject.scalc.domain.aggregate.expression.ExpressionUnit;
import local.myproject.scalc.domain.aggregate.expression.ParametersForExpressions;
import local.myproject.scalc.domain.aggregate.expression.ParametrisedExpressions;
import local.myproject.scalc.infrastructure.persistent.dao.expression.ExpressionUnitDao;
import local.myproject.scalc.infrastructure.persistent.dao.expression.ParametrisedExpressionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Реализация сервиса работы с выражениями.
 *
 * @author Evgenii Mironov
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExpressionLifecycleUseCase implements ExpressionUnitPort, ExpressionParameterPort {

    private final ExpressionUnitDao expressionUnitDao;
    private final ParametrisedExpressionDao parametrisedExpressionDao;
    private static Character counter = 'A';

    @Override
    /**
     * Возвращает выражение по идентификатору.
     *
     * @param expressionUnitId идентификатор выражения
     * @return найденное выражение
     */
    public ExpressionUnit findById(int expressionUnitId) {
        return expressionUnitDao.findById(expressionUnitId).orElseThrow();
    }

    @Override
    /**
     * Возвращает все выражения проекта и обновляет массив параметров.
     *
     * @param projectId идентификатор проекта
     * @return список выражений проекта
     */
    public List<ExpressionUnit> findAllByProjectId(int projectId) {
        return expressionUnitDao.findAllByProjectId(projectId);
    }

    /**
     * Возвращает выражения, отмеченные для наблюдения.
     *
     * @param projectId идентификатор проекта
     * @return список выражений из watch list
     */
    public List<ExpressionUnit> createWatchList(int projectId) {
        return expressionUnitDao.findAllByProjectId(projectId).stream().filter(x -> x.isWatchList()).toList();
    }

    @Override
    /**
     * Сохраняет выражение и связанные параметры.
     *
     * @param expressionUnit выражение для сохранения
     * @return результат не возвращается
     */
    public void save(ExpressionUnit expressionUnit) {
        if(checkDoubleExpression(expressionUnit)) {
            expressionUnit.setExpressionUnitName(this.setDefaultName(expressionUnit.getProject().getProjectId()));
            ExpressionUnit savedExpressionUnit = expressionUnitDao.save(expressionUnit);
            expressionUnit.setExpressionUnitId(savedExpressionUnit.getExpressionUnitId());
            this.findAndSaveOrUpdateParameters(expressionUnit);
        }
    }

    @Override
    /**
     * Обновляет выражение и связанные параметры.
     *
     * @param expressionUnit выражение для обновления
     * @return результат не возвращается
     */
    public void update(ExpressionUnit expressionUnit) {
            expressionUnitDao.update(expressionUnit);
            this.findAndSaveOrUpdateParameters(expressionUnit);
    }


    @Override
    /**
     * Удаляет выражение по идентификатору.
     *
     * @param expressionUnitId идентификатор выражения
     * @return результат не возвращается
     */
    public void deleteById(int expressionUnitId) {
        expressionUnitDao.deleteById(expressionUnitId);
    }


    /**
     * создает массив всех возможных параметров из списка выражений в виде массива [параметр][значение], где
     * параметр это имя выражения, а значение это результат вычисления этого выражения. массив записывается в
     * статический массив класса arrayOfParameters
     *
     * @param projectId идентификатор проекта
     * @return результат не возвращается
     */
    public String[][] findParametersByProjectId(int projectId) {
        List<ExpressionUnit> listOfExpressionUnit = expressionUnitDao.findAllByProjectId(projectId);
        String[][] parameter = new String[listOfExpressionUnit.size()][2];
        for(int i = 0; i < listOfExpressionUnit.size(); i++) {
            parameter[i][0] = listOfExpressionUnit.get(i).getExpressionUnitName();
            parameter[i][1] = listOfExpressionUnit.get(i).getExpressionResult();
        }
        return parameter;
    }

    /**
     * метод генерации имени выражения
     *
     * @param projectId идентификатор проекта
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
     *
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
     *
     * @param expressionUnit текущее выражение
     * @return результат не возвращается
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
