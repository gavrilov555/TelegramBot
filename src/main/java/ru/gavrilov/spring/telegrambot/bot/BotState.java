package ru.gavrilov.spring.telegrambot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Добрый день! ");
        }

        @Override
        public BotState nextState() {
            return EnterName;
        }
    },

    EnterName {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Укажите Ваше имя: ");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setUserName(context.getInput());
        }

        @Override
        public BotState nextState() {
            return EnterPhone;
        }

    },

    EnterPhone {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер телефона: ");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setPhone(context.getInput());
        }

        @Override
        public BotState nextState() {
            return EnterEmail;
        }
    },

    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите адрес электронной почты: ");
        }

        @Override
        public void handleInput(BotContext context) {
            String email = context.getInput();

            if (!Utils.isValidEmailAddress(email)) {
                sendMessage(context, "Упс, не верный формат электронной почты.");
                next = EnterEmail;
            } else {
                context.getUser().setEmail(context.getInput());
                next = Approved;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Approved(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Заявка успешно отправлена. ");
        }

        @Override
        public BotState nextState() {
            return Start;
        }
    };

    private static BotState[] states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }

        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {
        // do nothing by default
    }

    public abstract void enter(BotContext context);
    public abstract BotState nextState();
}
