//{{>licenseInfo}}
//        package {{package}};
//
//{{#operations}}
//
//public class {{classname}} {
//        String basePath = "{{basePath}}";
//        public static String appKey="";
//        public static String customerNo="";
//        public static String secretKey="";
//        public static String signNmame="";
//        public static String username="";
//        public static String password="";
//
//    public void setBasePath(String basePath) {
//        this.basePath = basePath;
//        }
//
//    public String getBasePath() {
//        return basePath;
//        }
//        {{#operation}}
//        /**
//         * {{summary}}
//         * {{notes}}
//         {{#allParams}}   * @param {{paramName}} {{description}}
//         {{/allParams}}   * @return {{#returnType}}{{{returnType}}}{{/returnType}}{{^returnType}}void{{/returnType}}
//         */
//    public {{#returnType}}{{{returnType}}} {{/returnType}}{{^returnType}}void {{/returnType}} {{nickname}}(
//        {{#allParams}}{{{dataType}}} {{paramName}}{{#hasMore}}, {{/hasMore}}{{/allParams}}){
//        {{#allParams}}{{#required}}
//        if ({{paramName}} == null){
//        System.out.println("'{{paramName}}'is null");
//        }
//        {{/required}}{{/allParams}}
//        String path = "{{path}}";
//        if(null!=appKey||null!=customerNo){
//        Map<String,String>params=new HashMap<String,String>();
//        {{#params}}
//        params.put("{{baseName}}", {{paramName}});
//        {{/params}}
//        YopClient.setAppKey(appKey);
//        YopClient.setCustomerNo(customerNo);
//        YopClient.setSecretKey(secretKey);
//        YopClient.setSignRet(true);
//        YopClient.setSignName(signName);
//        YopClient.setEncrypt(true);
//        YopClient.postData("{{basePath}{{path}}}",params);
//        }else{
//            if(OAuth2Client.getAuthenCredentials("{{basePath}{{path}}}",username,password)){
//                OAuth2Client.oauthCertificate("{{basePath}{{path}}}");
//        }
//        }
//        }
//
//        {{/operation}}
//}
//        {{/operations}}