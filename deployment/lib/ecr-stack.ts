import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { Repository, IRepository } from "aws-cdk-lib/aws-ecr";

interface EcrStackProps extends StackProps {
	repositoryNames: string[]
}

export class EcrStack extends Stack {
	private createdRepositories: [string, Repository][] = [];

	constructor(scope: Construct, id: string, props: EcrStackProps) {
		super(scope, id, props);

		props.repositoryNames.forEach(repositoryName => {
			this.createdRepositories.push([repositoryName, new Repository(this, repositoryName, {
				repositoryName: repositoryName
			})]);
		});
	}

	public getRepositoryByName(repositoryName: string): IRepository {
		return this.createdRepositories.find(repository => repository[0] == repositoryName)?.[1]
			|| {} as IRepository /** TODO: doesn't make sense */;
	}
}
