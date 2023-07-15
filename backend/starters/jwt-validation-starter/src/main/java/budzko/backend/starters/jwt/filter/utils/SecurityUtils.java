package budzko.backend.starters.jwt.filter.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class SecurityUtils {

    public static void processAccessDenied(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final var statusCode = HttpStatus.FORBIDDEN.value();
        final var msg = HttpStatus.FORBIDDEN.getReasonPhrase();
        //TODO
//        final var responseBody = RestExceptionDto.builder()
//                .exceptionId(UUID.randomUUID().toString())
//                .status(statusCode)
//                .message(msg)
//                .time(DateUtils.now())
//                .url(request.getRequestURL().toString())
//                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);
        //TODO
//        response.getWriter().write(JsonUtils.jsonMapper().writeValueAsString(responseBody));
        response.getWriter().write(msg);
    }
}
