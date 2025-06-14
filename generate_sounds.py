import numpy as np
from scipy.io import wavfile
import os

def create_directory():
    if not os.path.exists('sounds'):
        os.makedirs('sounds')

def generate_catch_sound():
    # Generate a short "pop" sound
    sample_rate = 44100
    duration = 0.2
    t = np.linspace(0, duration, int(sample_rate * duration))
    frequency = 880
    signal = np.sin(2 * np.pi * frequency * t) * np.exp(-5 * t)
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/catch.wav', sample_rate, signal)

def generate_bomb_sound():
    # Generate an explosion sound
    sample_rate = 44100
    duration = 0.5
    t = np.linspace(0, duration, int(sample_rate * duration))
    noise = np.random.normal(0, 1, len(t))
    envelope = np.exp(-3 * t)
    signal = noise * envelope
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/bomb.wav', sample_rate, signal)

def generate_powerup_sound():
    # Generate a rising tone
    sample_rate = 44100
    duration = 0.3
    t = np.linspace(0, duration, int(sample_rate * duration))
    frequency = np.linspace(440, 880, len(t))
    signal = np.sin(2 * np.pi * frequency * t)
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/powerup.wav', sample_rate, signal)

def generate_gameover_sound():
    # Generate a descending tone
    sample_rate = 44100
    duration = 1.0
    t = np.linspace(0, duration, int(sample_rate * duration))
    frequency = np.linspace(880, 220, len(t))
    signal = np.sin(2 * np.pi * frequency * t)
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/gameover.wav', sample_rate, signal)

def generate_combo_sound():
    # Generate a rising sequence of tones
    sample_rate = 44100
    duration = 0.4
    t = np.linspace(0, duration, int(sample_rate * duration))
    frequencies = [440, 554, 659, 880]
    signal = np.zeros_like(t)
    for i, freq in enumerate(frequencies):
        start = i * len(t) // len(frequencies)
        end = (i + 1) * len(t) // len(frequencies)
        signal[start:end] = np.sin(2 * np.pi * freq * t[start:end])
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/combo.wav', sample_rate, signal)

def generate_shield_sound():
    # Generate a shield activation sound
    sample_rate = 44100
    duration = 0.3
    t = np.linspace(0, duration, int(sample_rate * duration))
    frequency = 660
    signal = np.sin(2 * np.pi * frequency * t) * np.exp(-2 * t)
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/shield.wav', sample_rate, signal)

def generate_fruitmiss_sound():
    # Generate a "miss" sound
    sample_rate = 44100
    duration = 0.2
    t = np.linspace(0, duration, int(sample_rate * duration))
    frequency = 220
    signal = np.sin(2 * np.pi * frequency * t) * np.exp(-5 * t)
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/fruitmiss.wav', sample_rate, signal)

def generate_background_music():
    # Generate a simple background music loop
    sample_rate = 44100
    duration = 4.0
    t = np.linspace(0, duration, int(sample_rate * duration))
    
    # Create a simple melody
    frequencies = [440, 494, 523, 587, 659, 587, 523, 494]
    signal = np.zeros_like(t)
    for i, freq in enumerate(frequencies):
        start = i * len(t) // len(frequencies)
        end = (i + 1) * len(t) // len(frequencies)
        signal[start:end] = np.sin(2 * np.pi * freq * t[start:end])
    
    # Add some harmony
    harmony = np.sin(2 * np.pi * 330 * t) * 0.3
    
    # Combine and normalize
    signal = (signal + harmony) * 0.7
    signal = np.int16(signal * 32767)
    wavfile.write('sounds/background.wav', sample_rate, signal)

def main():
    create_directory()
    generate_catch_sound()
    generate_bomb_sound()
    generate_powerup_sound()
    generate_gameover_sound()
    generate_combo_sound()
    generate_shield_sound()
    generate_fruitmiss_sound()
    generate_background_music()
    print("Sound effects generated successfully!")

if __name__ == "__main__":
    main() 