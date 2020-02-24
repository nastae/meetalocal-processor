package lt.govilnius.domain.reservation;

import javax.persistence.*;

@Entity
@Table(name = "evaluation_entity")
public class Evaluation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "engagement_id")
    private MeetEngagement engagement;

    private String comment;

    private UserType evaluator;

    public Evaluation() {
    }

    public Evaluation(MeetEngagement engagement, String comment, UserType evaluator) {
        this.engagement = engagement;
        this.comment = comment;
        this.evaluator = evaluator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeetEngagement getEngagement() {
        return engagement;
    }

    public void setEngagement(MeetEngagement engagement) {
        this.engagement = engagement;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserType getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(UserType evaluator) {
        this.evaluator = evaluator;
    }
}
