package com.poultryfarm.services;

import javax.swing.*;

public class JMessageService implements MessageService {
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showExclamation(String exclamation) {
        JOptionPane.showMessageDialog(null, exclamation, "Exclamation", JOptionPane.WARNING_MESSAGE);
    }
}
