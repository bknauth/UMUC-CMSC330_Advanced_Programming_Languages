#include <iostream>
using namespace std;

#include "expression.h"
#include "subexpression.h"
#include "operand.h"
#include "plus.h"
#include "minus.h"
#include "times.h"
#include "divide.h"
#include "lessthan.h"
#include "greaterthan.h"
#include "equalto.h"
#include "or.h"
#include "and.h"

SubExpression::SubExpression(Expression* left, Expression* right)
{
	this->left = left;
	this->right = right;
}

Expression* SubExpression::parse()
{
	Expression* left;
	Expression* right;
	Expression* condition;
	char operation, paren, question;

	left = Operand::parse();
	cin >> operation;	
	if (operation != ':' && operation != '!')
	{
		right = Operand::parse();
		cin >> paren;
		switch (operation)
		{
			case '+':
				return new Plus(left, right);
			case '-':
				return new Minus(left, right);
			case '*':
				return new Times(left, right);
			case '/':
				return new Divide(left, right);
			case '<':
				return new LessThan(left, right);
			case '>':
				return new GreaterThan(left, right);
			case '=':
				return new EqualTo(left, right);
			case '|':
				return new Or(left, right);
			case '&':
				return new And(left, right);
		}
	}
	else if (operation == ':')//Since ':' isn't an <op>, I didn't want to make it a case with the other arithmetic operators.
	{
		right = Operand::parse();
		cin >> question;
		condition = Operand::parse();
		cin >> paren;

		if (condition->evaluate() == 1)
			return left;
		else
			return right;
	}
	else if (operation == '!')
	{
		
	}
	return 0;
}