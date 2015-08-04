class EqualTo : public SubExpression
{
public:
	EqualTo(Expression* left, Expression* right) :
		SubExpression(left, right)
	{
	}
	double evaluate()
	{
		if (left == right)
			return 1;
		else
			return 0;
	}
};