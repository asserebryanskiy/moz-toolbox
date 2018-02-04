import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by andreyserebryanskiy on 29/01/2018.
 */
public class Main {

    private static final String FLASH_MESSAGE_ATTR = "flashMessage";

    public static void main(String[] args) {
        staticFileLocation("/public");

        get("/random", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.session(false) != null) {
                model.put("from", req.session().attribute("from"));
                model.put("to", req.session().attribute("to"));
                model.put(FLASH_MESSAGE_ATTR, req.session().attribute(FLASH_MESSAGE_ATTR));
                req.session().removeAttribute(FLASH_MESSAGE_ATTR);
            }

            return render(model, "/random/index.hbs");
        });

        get("/random/generator", (req, res) -> {
            // assume we got wrong values (i.e. not integers)
            String fromStr = req.queryParams("from");
            String toStr = req.queryParams("to");
            int from;
            int to;
            try {
                from = Integer.parseInt(fromStr);
                to = Integer.parseInt(toStr);
            } catch (NumberFormatException nfe) {
                addErrorParams(req, fromStr, toStr, "Границы должным быть целым числом");
                res.redirect("/random");
                return null;
            }
            if (to <= from) {
                addErrorParams(req, fromStr, toStr, "Верхняя граница должна быть больше нижней");
                res.redirect("/random");
                return null;
            }
            Map<String, Object> model = new HashMap<>();
            model.put("from", from);
            model.put("to", to);
            return render(model, "/random/generator.hbs");
        });
    }

    private static void addErrorParams(Request req, String fromStr, String toStr, String message) {
        req.session().attribute("from", fromStr);
        req.session().attribute("to", toStr);
        req.session().attribute(FLASH_MESSAGE_ATTR, message);
    }

    private static String render(Map<String, Object> model, String viewName) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, viewName));
    }
}
