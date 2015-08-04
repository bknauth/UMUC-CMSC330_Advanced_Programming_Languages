class And : public SubExpression
{
public:
	And(Expression* left, Expression* right) :
		SubExpression(left, right)
	{
	}
	double evaluate()
	{
		if (left->evaluate() == 1 && right->evaluate() == 1)
			return 1;
		else
			return 0;
	}
};