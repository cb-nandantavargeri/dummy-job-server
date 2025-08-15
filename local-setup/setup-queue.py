#!/usr/bin/python3

import os
import boto3
from botocore.exceptions import ClientError

def main():
    endpoint = os.getenv("LOCALSTACK_URL")
    region = os.getenv("AWS_REGION")

    sqs = boto3.client(
        "sqs",
        endpoint_url=endpoint,
        region_name=region,
        aws_access_key_id=os.getenv("AWS_ACCESS_KEY_ID"),
        aws_secret_access_key=os.getenv("AWS_SECRET_ACCESS_KEY"),
    )

    try:
        resp = sqs.create_queue(QueueName="jobs-queue")
        print(f"Queue created (or already exists): {resp['QueueUrl']}")
    except ClientError as e:
        print(f"Failed to create queue: {e}")

if __name__ == "__main__":
    main()

