# Hanh trinh dong song - Release 2

Based on tag v2.0, with animated birds plus two fish, shrimp and turtle. Click or tap an animal for 10 points and a bubble for 1 point. Add an animal every 3 seconds with the on-screen button.

Rounds last 4 minutes. Bubbles fall at 40 pixels/second and animals emit bubbles every 2.4 seconds. Updated background music, pause/resume and local result history are included.

## Web

Open web/index.html locally, or upload the contents of web/ to any static host. No build step is required.

Live game: https://hanh-trinh-dong-song-cau-rac.nativeluck1.chatgpt.site

GitHub Actions validates and packages web/ on main pushes. For optional Pages deployment, enable GitHub Actions in Settings > Pages and set the Actions variable ENABLE_GITHUB_PAGES=true.

## Java desktop

Run gradlew.bat lwjgl3:run. The game uses the release v2.0 gameplay with the aquatic animals added.
