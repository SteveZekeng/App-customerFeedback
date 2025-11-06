package com.ccaBank.feedback.Services;

import com.ccaBank.feedback.Entities.Feedback;
import com.ccaBank.feedback.Entities.Response;
import com.ccaBank.feedback.Repositories.FeedbackRepository;
import com.ccaBank.feedback.Repositories.ResponseRepository;
import com.ccaBank.feedback.Repositories.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ResponseRepository responseRepository;
    private final StaffRepository staffRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           ResponseRepository responseRepository,
                           StaffRepository staffRepository) {
        this.feedbackRepository = feedbackRepository;
        this.responseRepository = responseRepository;
        this.staffRepository = staffRepository;
    }

    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public double computeAverageScore(Feedback feedback) {
        if (feedback.getResponse() == null || feedback.getResponse().isEmpty()) {
            return 0.0;
        }

        double total = feedback.getResponse()
                .stream()
                .mapToDouble(Response::getValue)
                .sum();

        return total / feedback.getResponse().size();
    }

}
