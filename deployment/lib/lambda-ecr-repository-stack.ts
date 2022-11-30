import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { Repository, IRepository } from "aws-cdk-lib/aws-ecr";

export class LambdaEcrRepositoryStack extends Stack {
	private createdLambdaEcrRepository: Repository;

	constructor(scope: Construct, id: string, props?: StackProps) {
		super(scope, id, props);

		this.createdLambdaEcrRepository = new Repository(this, "LambdaEcrRepositoryStack", {
			repositoryName: "lambda-ecr-repository"
		});
	}

	public getCreatedLambdaEcrRepository(): IRepository {
		return this.createdLambdaEcrRepository;
	}
}
