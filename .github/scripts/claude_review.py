import os
import anthropic
import requests
import json

# 환경 변수 가져오기
github_token = os.environ["GITHUB_TOKEN"]
anthropic_api_key = os.environ["ANTHROPIC_API_KEY"]
pr_number = os.environ["PR_NUMBER"]
repo_name = os.environ["REPO_NAME"]

# API 클라이언트 초기화
client = anthropic.Anthropic(api_key=anthropic_api_key)

# GitHub API 기본 URL
api_url = f"https://api.github.com/repos/{repo_name}"

# PR의 변경 파일 가져오기
headers = {
    "Authorization": "token {github_token}",
    "Accept": "application/vnd.github.v3+json"
}
files_response = requests.get(f"{api_url}/pulls/{pr_number}/files", headers=headers)
files = files_response.json()

# Diff 내용 수집
diff_content = ""
for file in files:
    diff_content += f"File: {file['filename']}\n{file.get('patch', '파일 내용 없음')}\n\n"

# Claude API 호출
response = client.messages.create(
    model="claude-3-7-sonnet-20250219",
    max_tokens=4000,
    messages=[
        {"role": "user", "content": f"""
        다음은 GitHub Pull Request #{pr_number}의 코드 변경사항입니다:

        ```diff
        {diff_content}
        ```

        이 코드 변경에 대한 상세한 리뷰를 제공해주세요:
        1. 코드 품질 평가
        2. 잠재적 버그나 이슈 탐지
        3. 성능 최적화 제안
        4. 보안 고려사항
        5. 코드 스타일 및 가독성 개선점

        가능하면 구체적인 개선 제안도 함께 제공해주세요.
        """}
    ]
)

review_comment = response.content[0].text

# PR에 리뷰 코멘트 게시
comment_data = {"body": review_comment}
requests.post(
    f"{api_url}/issues/{pr_number}/comments",
    headers=headers,
    json=comment_data
)

print("코드 리뷰가 PR에 게시되었습니다.")