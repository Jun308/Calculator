package Controller;

import GUI.Main;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JTextField;

public class Calculate {

    private BigDecimal firstNum;
    private BigDecimal secondNum;
    private final JTextField txtInput;
    private boolean reset;
    private boolean isMR = false;
    private boolean process;
    private int operator = -1;
    private boolean status = false;
    private BigDecimal memory = new BigDecimal("0");
//    private DecimalFormat df = new DecimalFormat("#.##########");

    public Calculate(JTextField text) {
        this.txtInput = text;
        operator = -1; // ?
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public boolean checkInput() {
        String display = txtInput.getText();

        if (display.contains(".")) {
            if (display.length() < 15) {
                return true;
            } else {
                return false;
            }
        } else {
            if (display.length() < 15) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void pressNumber(JButton btn) {
        BigDecimal temp;
        String value = btn.getText();
        if (process || reset) {
            txtInput.setText("0"); // ?
            process = false;
            reset = false;
        }
        temp = new BigDecimal(txtInput.getText() + value);
        if (temp.doubleValue() != 0) {
            txtInput.setText(temp.toPlainString() + "");
        } else if (txtInput.getText().equals("0")) {
            txtInput.setText(temp.toPlainString());
        } else {
            txtInput.setText(txtInput.getText() + value);
        }

        isMR = false;

    }

    public void pressDot() {
        if (process || reset) {
            txtInput.setText("0");
            process = false;
            reset = false;
        }
        if (!txtInput.getText().contains(".")) //neu txtInput k chua . thi them vao
        {
            txtInput.setText(txtInput.getText() + ".");
        }
    }

    public BigDecimal getValue() {
        if (isMR) {
            return memory;
        }
        String value = txtInput.getText();
        BigDecimal temp = new BigDecimal(value);
        return temp;
    }

    public void pressMR() {
        if (!txtInput.getText().equals("ERROR")) {
            txtInput.setText(memory + "");
            isMR = true;
        } else {
            txtInput.setText("ERROR");
        }
    }

    public void pressClear() {
        firstNum = new BigDecimal("0");
        secondNum = new BigDecimal("0");
        operator = -1;
    }

    public void calculate() {
        try {
            if (!process) {
                if (operator == -1) {
                    firstNum = getValue();
                } else {
                    secondNum = getValue();

                    switch (operator) {
                        case 1:
                            firstNum = firstNum.add(secondNum).stripTrailingZeros();
                            break;
                        case 2:
                            firstNum = firstNum.subtract(secondNum).stripTrailingZeros();
                            break;
                        case 3:

                            firstNum = firstNum.multiply(secondNum).stripTrailingZeros();
                            
                            break;
                        case 4:
                            //firstNum = firstNum.divide(secondNum);
                            if (firstNum.doubleValue() % secondNum.doubleValue() == 0) {
                                firstNum = firstNum.divide(secondNum);
                            } else {
                                double result = firstNum.doubleValue() / secondNum.doubleValue();
                                firstNum = new BigDecimal(result + "");
                            }
                            break;
                    }
                }
                txtInput.setText(firstNum.toPlainString() + "");
                process = true;
            }
        } catch (Exception e) {
            reset = true;
            txtInput.setText("ERROR");
        }
    }

    public void pressResult() {
        if (!txtInput.getText().equals("ERROR")) {
            calculate();
            operator = -1;
        } else {
            txtInput.setText(firstNum + "");
        }
    }

    public void pressNegate() {
        try {
            // check if txtInput already exist character dot
            if (txtInput.getText().contains(".")) {
                // check if the length of txtInput more than two character and end line is dot
                if (txtInput.getText().length() >= 2 && txtInput.getText().endsWith(".")) {
                    if (txtInput.getText().equals("0.")) {
                        txtInput.setText("-0.");
                    } else if (txtInput.getText().equals("-0.")) {
                        txtInput.setText("0.");
                    } else {
                        txtInput.setText(getValue().negate().toPlainString() + ".");
                    }
                } else if (getValue().doubleValue() == 0) {
                    if (txtInput.getText().startsWith("0.")) {
                        txtInput.setText("-" + txtInput.getText());
                    } else if (txtInput.getText().startsWith("-0.")) {
                        txtInput.setText(txtInput.getText().replace("-", ""));
                    } else {
                        txtInput.setText(getValue().negate().toPlainString() + "");
                    }
                } else {
                    txtInput.setText(getValue().negate().toPlainString() + "");
                }
            } else {
//                pressResult();
                txtInput.setText(getValue().negate().toPlainString() + ""); // return the negative of number
            }
            process = false; // process set to false to do any operator
        } catch (Exception e) {
            txtInput.setText("ERROR");
        }
        // if user click result or operator
        if (status) {
            reset = true;
        } else {
            reset = false;
        }
    }

    public void pressSqrt() {
        try {
            if (!txtInput.getText().equals("ERROR")) {
                pressResult();
                BigDecimal result = getValue();
                if (result.doubleValue() >= 0) {
                    String display = Math.sqrt(result.doubleValue()) + "";
                    if (display.endsWith(".0")) {
                        display = display.replace(".0", "");
                    }
                    txtInput.setText(display);
                    process = false;
                } else {
                    txtInput.setText("ERROR");
                }
            } else {
                txtInput.setText("ERROR");
            }
        } catch (Exception e) {
            txtInput.setText("ERROR");
        }
        reset = true;
        status = true;
    }

    public void pressPercent() {
        try {
            if (!txtInput.getText().equals("ERROR")) {
                pressResult();
                txtInput.setText(getValue().divide(new BigDecimal(100)).stripTrailingZeros().toPlainString() + "");
                process = false;
            } else {
                txtInput.setText("ERROR");
            }
        } catch (Exception e) {
            txtInput.setText("ERROR");
        }
        reset = true;
        status = true;
    }
    public void pressInvert() {
        if (!txtInput.getText().equals("ERROR")) {

            pressResult();
            double result = getValue().doubleValue();
            BigDecimal value1 = new BigDecimal("1");
            BigDecimal value = new BigDecimal(1 / result);
            //value = value.divide(value1, 15, RoundingMode.HALF_UP);

            if (result != 0) {
                txtInput.setText(value.stripTrailingZeros().toPlainString());
                process = false;
            } else {
                txtInput.setText("ERROR");
            }
        } else {
            txtInput.setText("ERROR");
        }
        reset = true;
        status = true;
    }

    //MC : xóa bỏ trong bộ nhớ xét memory =0
    //MR: lấy ra giá trị trong bộ nhớ 
    //ấn số: => M+ : lưu giá trị đó vào memory
    //ấn số: => M- : lưu giá trị đối của nó vào memory
    //Nhấn M+ / M- => MR đổi màu
    //Nhấn MC: => MR về màu cũ
    public void pressMC() {
        memory = new BigDecimal("0");
        process = true;
        reset = false;
    }

    public void pressMAdd() {
        try {
            if (!txtInput.getText().equals("ERROR")) {
            memory = memory.add(getValue());
            process = false;
            }else{
                 txtInput.setText("ERROR");
            }
        } catch (Exception e) {
            txtInput.setText("ERROR");
        }
        reset = true;
    }

    public void pressMSub() {
        try {
            if (!txtInput.getText().equals("ERROR")) {
            memory = memory.add(getValue().negate());
            process = false;
            }else{
                txtInput.setText("ERROR");
            }
        } catch (Exception e) {
            txtInput.setText("ERROR");
        }
        reset = true;
    }

//    public void removeLast() {
//        String txt = txtInput.getText();
//        if (txt.length() == 2 && txt.startsWith("-")) {
//            txtInput.setText("0");
//            return;
//        }
//        if (txt.length() == 1) {
//            txtInput.setText("0");
//            return;
//        }
//        txt = txt.substring(0, txt.length() - 1);
//        txtInput.setText(txt);
//    }
    public void removeLast() {
        if (reset != true) {
            if (process != true) {
                String str = txtInput.getText();
                String result = "0";
                if (str.length() > 1) {
                    result = str.substring(0, str.length() - 1);
                }
                if (str.length() == 1) {
                    result = "0";
                } else if (str.startsWith("-") && str.length() == 2) {
                    result = "0";
                }
                // setText(result);
                txtInput.setText(result + "");
            }
        }
    }
}
