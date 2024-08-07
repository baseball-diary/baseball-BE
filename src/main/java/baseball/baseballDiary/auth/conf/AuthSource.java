package baseball.baseballDiary.auth.conf;

public interface AuthSource {

    String authorize();

    String accessToken();

    String userInfo();

    String profileUrl();

    void setAccessToken(String url);


    default String getName() {
        if (this instanceof Enum) {
            return String.valueOf(this);
        }
        return this.getClass().getSimpleName();
    }

}
