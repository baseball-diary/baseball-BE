package baseball.baseballDiary.auth.conf;

public enum AuthDefaultSource implements AuthSource{

    KAKAO {
        private String authUrl = "https://kauth.kakao.com/oauth/authorize";
        private String accessUrl = "https://kauth.kakao.com/oauth/token";
        private String profileUrl = "https://kapi.kakao.com/v2/user/me";

        @Override
        public String authorize() {
            return authUrl;
        }

        @Override
        public String accessToken() {
            return accessUrl;
        }

        @Override
        public String profileUrl() {
            return profileUrl;
        }

        @Override
        public String userInfo() {
            return "";
        }

        @Override
        public void setAccessToken(String url) {
            this.accessUrl = url;
        }
    }

}
