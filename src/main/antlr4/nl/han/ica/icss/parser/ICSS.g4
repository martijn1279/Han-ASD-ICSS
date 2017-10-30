grammar ICSS;

@header{
package nl.han.ica.icss.parser;
}

stylesheet: stylesheetPart*;

stylesheetPart: assignment | stylerule;

stylerule:  (selector statement? STYLERULE_OPEN_CURLY declaration+ STYLERULE_CLOSE_CURLY);


selector: ((SELECTOR_TAG | SELECTOR_ID) | SELECTOR_CLASS);

statement: STYLERULE_OPEN_BRACKETS  expression additionalExpression*  STYLERULE_CLOSE_BRACKETS;
additionalExpression: logicalOperator expression;


declaration: ATTRIBUTES ATTRIBUTE_VALUE_SEPERATOR expression LINE_END;

expression: operation| value;
operation: value (operator value);
value: (literal | variableReference);

operator: calcOperator | relationalOperator;
calcOperator: CALC_OPERATOR_ADD | CALC_OPERATOR_SUB | CALC_OPERATOR_DIV | CALC_OPERATOR_MUL;
relationalOperator: CONDITION_OPERATOR_GT | CONDITION_OPERATOR_LW | CONDITION_OPERATOR_EQ;
logicalOperator: (CONDITION_OPERATOR_OR | CONDITION_OPERATOR_AND);

literal: LITERAL_COLOR | LITERAL_PIXELS | LITERAL_PERCENTAGE | LITERAL_BOOL;

variableReference: CONSTANT_NAME;

assignment: variableReference CONSTANT_ASSIGNMENT_SEPERATOR expression LINE_END;

WS: [ \t\r\n]+ -> skip;

STYLERULE_OPEN_CURLY:  '{';
STYLERULE_CLOSE_CURLY: '}';
STYLERULE_OPEN_BRACKETS:  '[';
STYLERULE_CLOSE_BRACKETS: ']';

LINE_END: ';';

ATTRIBUTES: 'color' | 'background-color' | 'width' | 'height';

ATTRIBUTE_VALUE_SEPERATOR: ':';

LITERAL_COLOR: '#'[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f];
LITERAL_PIXELS: [0-9]+'px';
LITERAL_PERCENTAGE: [0-9]+'%';
LITERAL_BOOL: 'true' | 'false';

SELECTOR_ID: '#'[a-z0-9_]+;
SELECTOR_CLASS: '.'[a-z0-9_]+;
SELECTOR_TAG: [a-z0-9_]+;

CONSTANT_NAME: [A-Z0-9_]+;
CONSTANT_ASSIGNMENT_SEPERATOR: ':=';

CALC_OPERATOR_ADD:'+' ;
CALC_OPERATOR_SUB: '-';
CALC_OPERATOR_DIV:'*';
CALC_OPERATOR_MUL:'/';


CONDITION_OPERATOR_OR:'||';
CONDITION_OPERATOR_AND:'&&' ;
CONDITION_OPERATOR_GT:'>';
CONDITION_OPERATOR_LW:'<';
CONDITION_OPERATOR_EQ: '==';

