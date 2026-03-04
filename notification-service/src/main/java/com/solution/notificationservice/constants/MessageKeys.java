package com.solution.notificationservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageKeys {
    public static final String BINDING_WELCOME = "bot.binding.welcome";
    public static final String BINDING_SUCCESS = "bot.binding.success";
    public static final String BINDING_ERR_INVALID = "bot.binding.error.invalid";
    public static final String BINDING_ERR_INTERNAL = "bot.binding.error.internal";
    public static final String BINDING_REQUIRED = "bot.binding.required";

    public static final String UNBIND_SUCCESS = "bot.unbind.success";
    public static final String UNBIND_ERROR = "bot.unbind.error";
    public static final String UNBIND_NOTFOUND =  "bot.unbind.notfound";

    public static final String HELP_AUTHORIZED = "bot.help.authorized";
    public static final String HELP_UNAUTHORIZED = "bot.help.unauthorized";

    public static final String UNKNOWN = "bot.unknown";
    public static final String SERVICES_EMPTY = "bot.services.empty";
    public static final String ERROR_INTERNAL = "bot.error.internal";
}
