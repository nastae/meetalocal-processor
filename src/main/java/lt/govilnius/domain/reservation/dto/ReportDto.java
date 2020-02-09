package lt.govilnius.domain.reservation.dto;

public class ReportDto extends TokenDto {

    private String comment;

    public ReportDto(String token, String comment) {
        super(token);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
