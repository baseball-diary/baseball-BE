package baseball.baseballDiary.common.handler;

import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.dto.ResponseDto;
import baseball.baseballDiary.common.exception.BaseValidateException;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.validation.ValidationBingingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
@Order(-2)
@RequiredArgsConstructor
public class CommonErrorExceptionHandler {
    private final ValidationBingingResult validationBingingResult;

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity renderErrorResponse(Exception e) {

        ResponseDto<Object> responseDto = ResponseDto.builder().build();

        // Default Error
        responseDto.setError(ApiState.SYSTEM.getCode(), "System error");

        if (e instanceof CommonLogicException) {
            CommonLogicException ex = (CommonLogicException) e;

            responseDto.setError(ex.getCode(), ex.getExMessage(ex.getCode(), ex.getMessageKey(), ex.getArgs()));

            if (ArrayUtils.isNotEmpty(ex.getValidateMessage())) {
                responseDto.setData(ex.getValidateMessage());
            }
        } else if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();

            try {
                validationBingingResult.checkParamsCommon(bindingResult);
            } catch (CommonLogicException ex) {
                responseDto.setError(ex.getCode(), ex.getExMessage(ex.getCode(), ex.getMessageKey(), ex.getArgs()));

                if (ArrayUtils.isNotEmpty(ex.getValidateMessage())) {
                    responseDto.setData(ex.getValidateMessage());
                }
            }
        } else if (e instanceof BaseValidateException) {
            responseDto.setError(ApiState.VALIDATE.getCode(), e.getMessage());
        }

        log.error("Unknown System Error: ", e);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }
}
