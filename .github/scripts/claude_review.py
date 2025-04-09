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
    "Authorization": f"token {github_token}",
    "Accept": "application/vnd.github.v3+json"
}

print(f"PR 번호: {pr_number}")
print(f"저장소명: {repo_name}")

try:
    files_url = f"{api_url}/pulls/{pr_number}/files"
    files_response = requests.get(files_url, headers=headers)

    if files_response.status_code != 200:
        print(f"GitHub API 오류: {files_response.status_code} - {files_response.text}")
        exit(1)

    # 응답을 JSON으로 파싱
    files = files_response.json()

    # Diff 내용 수집
    diff_content = ""
    if isinstance(files, list):
        for file in files:
            if isinstance(file, dict):
                filename = file.get('filename', 'Unknown')
                patch = file.get('patch', '파일 내용 없음')
                diff_content += f"File: {filename}\n{patch}\n\n"

        print(f"총 {len(files)}개 파일의 변경사항을 수집했습니다.")
    else:
        print("오류: 파일 목록이 리스트 형식이 아닙니다.")
        exit(1)

    if not diff_content:
        print("변경된 내용이 없거나 패치 정보를 가져올 수 없습니다.")
        exit(0)

    print("코드 리뷰 요청을 Claude API에 전송 중...")

    # PR 제목과 설명 가져오기
    pr_url = f"{api_url}/pulls/{pr_number}"
    pr_response = requests.get(pr_url, headers=headers)
    pr_data = pr_response.json()
    pr_title = pr_data.get('title', 'No title')
    pr_description = pr_data.get('body', 'No description')

    # Claude API 호출
    prompt = f"""
    GitHub Pull Request #{pr_number} "{pr_title}"의 코드 리뷰를 수행해주세요.

    PR 설명:
    {pr_description}

    변경된 파일 내용:
    ```diff
    {diff_content}
    ```

    이 코드 변경에 대한 상세한 리뷰를 한글로 제공해주세요:
    1. 코드 품질 평가
    2. 잠재적 버그나 이슈 탐지
    3. 성능 최적화 제안
    4. 보안 고려사항
    5. 코드 스타일 및 가독성 개선점

    가능하면 구체적인 개선 제안도 함께 제공해주세요.
    코드 리뷰는 마크다운 형식으로 깔끔하게 작성해주세요.
    """

    response = client.messages.create(
        model="claude-3-7-sonnet-20250219",
        max_tokens=4000,
        messages=[
            {"role": "user", "content": prompt}
        ]
    )

    review_comment = response.content[0].text

    print("Claude의 코드 리뷰 완료, PR에 코멘트 게시 중...")

    # PR에 리뷰 코멘트 게시
    comment_data = {
        "body": f"## Claude AI 코드 리뷰\n\n{review_comment}"
    }

    comment_url = f"{api_url}/issues/{pr_number}/comments"
    comment_response = requests.post(
        comment_url,
        headers=headers,
        json=comment_data
    )

    if comment_response.status_code in [201, 200]:
        print("코드 리뷰가 PR에 성공적으로 게시되었습니다.")
    else:
        print(f"코멘트 게시 실패: {comment_response.status_code} - {comment_response.text}")

except Exception as e:
    print(f"오류 발생: {str(e)}")
    exit(1)