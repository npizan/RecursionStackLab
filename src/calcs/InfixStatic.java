/**
 * 
 */
package calcs;

/**
  * This is class InFixStatic. It is a static version
  * of the class InFixCalc. It will input a 
  * regular mathmetic expression and will turn 
  * that expression into the postfix form.  It will 
  * first split the expression up so it can deal with
  * it one token at a time.  It will then validate that the 
  * expression is valid and compute it to a postfix expression.  
  * @author Will Graham, Ryan Godfrey
  * @version 10/30/2016
  */
public class InfixStatic {
  /*public InfixCalc(String expression) {
    this.expression = expression;
    this.postExpression = "";
    validate();
    compute();
  }*/
  
  /**
   * 
   * @param expression
   */
  public static void run(String expression) {
    validate(expression);
    compute(expression);
  }
  

  /**
   * The validate() method will check each char and 
   * check for parenthesis matching.  If the expression
   * has an unbalanced number of parenthesis, this method
   * will throw an ArithmeticException.  
   */
  private static void validate(String expression) {
    Stack stack = new Stack();
    for (char c : expression.toCharArray()) {
      if (c == '(') {
        stack.push("(");
      } else if (c == ')') {
        if (stack.isEmpty()) {
          throw new ArithmeticException("Parenthesis Mismatch.");
        } else if (stack.peek() == "(") {
          stack.pop();
        } else
          throw new ArithmeticException("Parenthesis Mismatch.");
      }
    }
    if (!stack.isEmpty())
      throw new ArithmeticException("Parenthesis Mismatch.");
  }

  /**
   * The compute() method will create the stack
   * that is used to orginize the postfix expression.  It 
   * will properly split the expression up and sort the
   * tokens one at a time.  It will then use the stack to 
   * build the expression to a postfix form.  
   *
   */
  private static void compute(String expression) {
    String postExpression = "";
    Stack opStack = new Stack();
    String splitOperWhiteSpace = "(((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))|\\s+)";
    String[] exprArray = expression.trim().split(splitOperWhiteSpace);
    for (String token : exprArray) {
      if (isNumber(token)) { // Number
        postExpression += token + " ";
      } else if (token.equals(")")) {
        while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
          postExpression += opStack.pop() + " ";
        }
        opStack.pop(); // Remove open paren
      } else if (token.equals("(")) {
        opStack.push(token);
      } else if (isOperator(token)) {
        if (opStack.isEmpty()) {
          opStack.push(token);
        } else if (!isOperator(opStack.peek())) {
          opStack.push(token);
        } else if (getPrecedence(token) > getPrecedence(opStack.peek())) {
          opStack.push(token);
        } else {
          while(opStack.peek() != null && getPrecedence(token) <= getPrecedence(opStack.peek())) {
            postExpression += opStack.pop() + " ";
          }
          opStack.push(token);
        }
      }
    }
    while (!opStack.isEmpty()) {
      //TODO: Determine whether this check is necessary after opStack.pop(); on line 55
      if(opStack.peek().equals("(")) {
        opStack.pop();
      }
      else {
        postExpression += opStack.pop() + " ";
      }
    }
    System.out.println(postExpression); // TODO: Remove test statement
    new PostCalc(postExpression);
    
  }


  /**
   * The inNumber() method uses a regex expression
   * to match the char in question to a number.
   * @param s the element in question
   * @return a boolean expression if it is a number.  
   */
  private static boolean isNumber(String s) {
    return s.matches("^-?\\d+$");
  }

  /**
  * The inOperator() method uses a regex expression
  * to match the char in question to an operator.
  * @param s the element in question
  * @return a boolean expression if it is an operator.  
  */
  private static boolean isOperator(String s) {
    return s.trim().matches("^[+-/*//]$");
  }

  /**
   * The getPrecedence() method will assign an int value
   * to each operator so the compute() method will be able
   * to check each operator for priority level and push
   * to the stack or the String result.   
   * @param op the operator in question
   * @return the precedence level. 
   */
  private static int getPrecedence(String op) {
    switch (op.trim()) {
      case "+":
      case "-":
        return 1;
      case "*":
      case "/":
        return 2;
      default:
        return -1; // Error; should be unreachable
    }
  }
}
