package ru.iopump.portal.jira.service;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static java.lang.String.format;

@UtilityClass
class JiraServiceUtil {
    @NonNull <T> Response<T> execute(Call<T> call) {
        try {
            val res = call.execute();
            if (!res.isSuccessful()) {
                throw new HttpException(res);
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(format(
                    "Error [%s] during request to '%s %s' with body '%s'",
                    e.getLocalizedMessage(),
                    call.request().method(),
                    call.request().url().toString(),
                    getBodyAsString(call.request())
            ), e);
        }
    }

    @NonNull <T> Collection<T> collect(Response<Collection<T>> response) {
        if (response.body() == null) {
            return Collections.emptyList();
        } else {
            return response.body();
        }
    }

    @NonNull <T> T body(Response<T> response, T defaultBody) {
        if (response.body() == null) {
            return defaultBody;
        } else {
            return response.body();
        }
    }

    @NonNull <T> T body(Response<T> response) {
        return body(response, null);
    }

    @NonNull
    private String getBodyAsString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() == null) {
                return "empty";
            }
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "empty";
        }
    }
}