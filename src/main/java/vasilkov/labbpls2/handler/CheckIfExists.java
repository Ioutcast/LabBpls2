package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.entity.Model;
import vasilkov.labbpls2.repository.ModelRepository;
import vasilkov.labbpls2.repository.ProductRepository;

@Named("checkIfExists")
@RequiredArgsConstructor
@Slf4j
public class CheckIfExists implements JavaDelegate {
    private final ProductRepository productRepository;
    private final ModelRepository modelRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Model model =  modelRepository.findModelByName(delegateExecution.getVariable("modelNameId").toString()).get();
        if (productRepository.existsByModelId(model.getId())>0) {
            delegateExecution.removeVariable("inBucket");
        } else throw new Exception();
    }

}
