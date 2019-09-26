package site.lilpig.gayhub.bookcrawler.core;

import androidx.annotation.Nullable;

import okhttp3.Response;

public interface TaskCallback {
    void done(@Nullable Response response);
}
