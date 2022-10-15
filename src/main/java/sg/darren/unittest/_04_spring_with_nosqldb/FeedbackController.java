package sg.darren.unittest._04_spring_with_nosqldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

}
