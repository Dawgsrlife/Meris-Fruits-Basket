@echo off
mkdir sounds 2>nul

echo Downloading sound effects...
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/catch.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/bomb.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/powerup.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/gameover.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/combo.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/shield.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/fruitmiss.wav"
curl -L "https://freesound.org/data/previews/131/131142_2337290-lq.mp3" -o "sounds/background.wav"

echo Sound effects downloaded successfully! 