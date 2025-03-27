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

# API 응답 디버깅
print(f"PR 번호: {pr_number}")
print(f"저장소명: {repo_name}")

try:
    files_url = f"{api_url}/pulls/{pr_number}/files"
    print(f"API 요청 URL: {files_url}")

    files_response = requests.get(files_url, headers=headers)
    print(f"API 응답 상태 코드: {files_response.status_code}")
    print(f"API 응답 내용: {files_response.text[:100]}...")  # 처음 100자만 출력

    # 응답을 JSON으로 파싱
    files = files_response.json()
    print(f"파싱된 파일 개수: {len(files) if isinstance(files, list) else 'Not a list'}")

    # 파일 타입 확인
    print(f"Files 타입: {type(files)}")

    # Diff 내용 수집
    diff_content = ""
    if isinstance(files, list):
        for file in files:
            if isinstance(file, dict):
                diff_content += f"File: {file.get('filename', 'Unknown')}\n{file.get('patch', '파일 내용 없음')}\n\n"
            else:
                print(f"Warning: file is not a dict: {type(file)}")
    else:
        diff_content = "파일 목록을 가져오는데 실패했습니다."
        print(f"Error: files is not a list: {files}")

    # API 호출만 수행하고 종료 (디버깅용)
    print("디버깅을 위해 여기서 종료합니다.")
    exit(0)

except Exception as e:
    print(f"오류 발생: {str(e)}")
    exit(1)