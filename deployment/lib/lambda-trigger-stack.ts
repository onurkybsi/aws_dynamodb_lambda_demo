import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import * as events from 'aws-cdk-lib/aws-events';
import { Function } from 'aws-cdk-lib/aws-lambda';
import { LambdaFunction } from 'aws-cdk-lib/aws-events-targets';

interface LambdaTriggerStackProps extends StackProps {
	currentPriceLoaderLambda: Function
}

export class LambdaTriggerStack extends Stack {

	constructor(scope: Construct, id: string, props: LambdaTriggerStackProps) {
		super(scope, id, props);

		const currentPriceLoaderTriggerRule = new events.Rule(this, "CurrentPriceLoaderTriggerRule", {
			schedule: events.Schedule.expression("rate(1 minute)"),
		});
		currentPriceLoaderTriggerRule.addTarget(new LambdaFunction((props.currentPriceLoaderLambda)));
	}
}