grammar ICSS;

stylesheet: stylesheetpart*;

stylesheetpart: constantassignment | stylerule;

stylerule: (selector STYLERULE_OPEN_CURLY declerationpart* STYLERULE_CLOSE_CURLY) | (selector STYLERULE_OPEN_BRACKETS selectorconditionpart STYLERULE_CLOSE_BRACKETS STYLERULE_OPEN_CURLY declerationpart* STYLERULE_CLOSE_CURLY) ;

selector: ((SELECTOR_ELEEMNT | SELECTOR_ID) | SELECTOR_CLASS);

selectorconditionpart: selectorcondition* | selectorcondition* CONDITIONSPLIT selectorcondition*;
selectorcondition: constantreference | (value CONDITIONOPERATOR_GREATERTHEN value) | (value CONDITIONOPERATOR_SMALLERTHEN value);

declerationpart: decleration | stylerule;
decleration: attribute ATTRIBUTE_VALUE_SEPERATOR value LINEEND;

attribute: ATTRIBUTE_COLOR | ATTRIBUTE_BACKGROUND_COLOR | ATTRIBUTE_WIDTH | ATTRIBUTE_HEIGHT;

value: realvalue | som;
som: realvalue (calcoperator realvalue)+;
realvalue: (literal | constantreference);

literal: LITERAL_COLOR | LITERAL_PIXELS | LITERAL_PERCENTAGE | LITERAL_TRUE | LITERAL_FALSE;

calcoperator: CALCOPERATOR_ADD | CALCOPERATOR_SUB | CALCOPERATOR_MUL | CALCOPERATOR_DEV;

constantreference: CONSTANT_NAME;

constantassignment: constantreference CONSTANT_ASSIGNMENT_SEPERATOR literal LINEEND;

WS: [ \t\r\n]+ -> skip;

STYLERULE_OPEN_CURLY:  '{';
STYLERULE_CLOSE_CURLY: '}';
STYLERULE_OPEN_BRACKETS:  '[';
STYLERULE_CLOSE_BRACKETS: ']';

LINEEND: ';';

ATTRIBUTE_COLOR: 'color';
ATTRIBUTE_BACKGROUND_COLOR: 'background-color';
ATTRIBUTE_WIDTH: 'width';
ATTRIBUTE_HEIGHT: 'height';

ATTRIBUTE_VALUE_SEPERATOR: ':';

LITERAL_COLOR: '#'[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f];
LITERAL_PIXELS: [0-9]+'px';
LITERAL_PERCENTAGE: [0-9]+'%';
LITERAL_TRUE: 'true';
LITERAL_FALSE: 'false';

SELECTOR_ID: '#'[a-z0-9_]+;
SELECTOR_CLASS: '.'[a-z0-9_]+;
SELECTOR_ELEEMNT: [a-z0-9_]+;

CONSTANT_NAME: [A-Z0-9_]+;
CONSTANT_ASSIGNMENT_SEPERATOR: ':=';

CALCOPERATOR_ADD: '+';
CALCOPERATOR_SUB: '-';
CALCOPERATOR_MUL: '*';
CALCOPERATOR_DEV: '/';

CONDITIONSPLIT: '||';
CONDITIONOPERATOR_GREATERTHEN: '>';
CONDITIONOPERATOR_SMALLERTHEN: '<';