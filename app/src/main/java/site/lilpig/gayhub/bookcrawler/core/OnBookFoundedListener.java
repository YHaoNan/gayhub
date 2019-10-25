package site.lilpig.gayhub.bookcrawler.core;

public interface OnBookFoundedListener {
    void onFound(Book book);
    void onTaskOver();
}
