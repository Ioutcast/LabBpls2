package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.repository.BrandRepository;
import vasilkov.labbpls2.repository.ModelRepository;

@Named("checkOrderRequest")
@RequiredArgsConstructor
@Slf4j
public class CheckOrderRequest implements JavaDelegate {
    private final ModelRepository modelRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        if (modelRepository.findModelByName(delegateExecution.getVariable("modelNameId").toString()).isPresent()) {
            delegateExecution.removeVariable("isBucket");
        } else throw new Exception();
    }
}
