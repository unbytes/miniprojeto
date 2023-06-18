package views;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import controllers.BranchController;
import views.components.Title;
import views.components.Button;
import views.layouts.BasicFrame;

public class LoginBranch extends BasicFrame {
    private BranchController branchController = new BranchController();

    public LoginBranch() {
        super();
        makeBody();
    }

    public void makeBody() {
        bodyPanel.setLayout(new BorderLayout());

        makeBranchList();

        this.add(bodyPanel, BorderLayout.CENTER);
    }

    public void makeBranchList() {
        Title titleLabel = new Title("Branches");
        bodyPanel.add(titleLabel, BorderLayout.NORTH);

        HashMap<String, String> branchesAsHTMLTemplate = branchController.getBranchesAsHTMLTemplate();
        String branches[] = new String[branchesAsHTMLTemplate.values().size()];

        Integer index = 0;
        for (String branch : branchesAsHTMLTemplate.values()) {
            branches[index] = branch;
            index++;
        }
        JList<String> branchList = new JList<String>(branches);
        bodyPanel.add(branchList, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        bodyPanel.add(buttonPanel, BorderLayout.SOUTH);

        Button registerButton = new Button("Criar nova");
        registerButton.addActionListener(e -> {
            this.dispose();
            new CreateBranch();
        });
        buttonPanel.add(registerButton);

        Button loginButton = new Button("Logar");
        loginButton.addActionListener(e -> {
            String selectedValue = branchList.getSelectedValue();
            String branchUUID = selectedValue.split("<br>")[0].split(": ")[1].trim();
            handleBranchPopUpLogin(branchUUID);
        });
        buttonPanel.add(loginButton);
    }

    public void handleBranchPopUpLogin(String branchUUID) {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showOptionDialog(null, passwordField, "Enter Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            boolean response = branchController.authenticateBranch(branchUUID, password);
            if (response) {
                this.dispose();
                new ManageBranch(branchUUID);
            } else {
                JOptionPane.showMessageDialog(null, "Wrong password");
            }
        }
    }
}
