package baseball.baseballDiary.common.utils;

import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.dto.ResponseDto;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.validation.ValidationBingingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
@Component
@Slf4j
public class ExceptionHandlerUtil {
    private final ValidationBingingResult validationBingingResult;

    public ResponseEntity renderErrorResponse(Exception e) {

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
        }

        log.error("Unknown System Error: ", e);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }
}
