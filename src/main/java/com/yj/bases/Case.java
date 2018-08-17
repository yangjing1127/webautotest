package com.yj.bases;

/**
 * 用例Model
 */
public class Case {
    private String caseId;
    private String apiId;
    private String params;
    private  String expectedReponseData;
    private String actualResponseData;

    @Override
    public String toString() {
        return "Case{" +
                "caseId='" + caseId + '\'' +
                ", apiId='" + apiId + '\'' +
                ", param='" + params + '\'' +
                ", expectResponseData='" + expectedReponseData + '\'' +
                ", actualResponseData='" + actualResponseData + '\'' +
                '}';
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String param) {
        this.params = param;
    }

    public String getExpectedReponseData() {
        return expectedReponseData;
    }

    public void setExpectedReponseData(String expectedReponseData) {
        this.expectedReponseData = expectedReponseData;
    }

    public String getActualResponseData() {
        return actualResponseData;
    }

    public void setActualResponseData(String actualResponseData) {
        this.actualResponseData = actualResponseData;
    }
}
