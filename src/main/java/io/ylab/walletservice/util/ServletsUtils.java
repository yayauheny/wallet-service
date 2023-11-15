package io.ylab.walletservice.util;

import io.ylab.walletservice.api.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.PrintWriter;

@UtilityClass
public class ServletsUtils {

    public static void setJwtTokenToResponse(String token, HttpServletResponse resp) throws IOException {
        resp.setHeader("Authorization", "Bearer: " + token);
        resp.getWriter().print(token);
        resp.getWriter().flush();
    }

    public static String getTokenFromRequest(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer: ")) {
            return token.substring(8);
        }
        return null;
    }

    public static boolean hasAdminRole(String token) {
        String role = JwtProvider.getPayload(token).get("role", String.class);
        return role.equalsIgnoreCase("admin");
    }

    public static void processRequestForDisallowedRole(HttpServletResponse resp, PrintWriter writer) throws IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        writer.print("Method not allowed.");
        writer.flush();
    }

    public static void processRequestForDisallowedAction(HttpServletResponse resp, PrintWriter writer) throws IOException {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        writer.print("You are not allowed to access information about other users.");
        writer.flush();
    }

    public static void setOkAndFlush(HttpServletResponse resp, PrintWriter writer, String jsonResponse) {
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.print(jsonResponse);
        writer.flush();
    }

    public static void setCreatedAndFlush(HttpServletResponse resp, PrintWriter writer, String jsonResponse, String resourceLocation) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location", resourceLocation);
        writer.print(jsonResponse);
        writer.flush();
    }

    public static void setBadRequestAndFlush(HttpServletResponse resp, PrintWriter writer) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.print("Invalid request: No data provided.");
        writer.flush();
    }

    public static void setNoContentAndFlush(HttpServletResponse resp, PrintWriter writer) {
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        writer.print("Resource successfully deleted.");
        writer.flush();
    }

}
