package ma.glasnost.orika.test.packageprivate;

class SimilarEntityCustomConstructor {
    private String field;

    public SimilarEntityCustomConstructor(String field) {
        this.field = field;
    }
    
    public String getField() {
        return field;
    }

}
