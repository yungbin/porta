<template>
  <div class="timer-input">
    <div class="time-section">
      <button @click="incrementMinutes" class="time-btn">▲</button>
      <input
        type="number"
        :value="minutes"
        @input="handleMinutesChange"
        min="0"
        max="59"
        class="time-input"
      />
      <button @click="decrementMinutes" class="time-btn">▼</button>
    </div>
    <span class="time-separator">:</span>
    <div class="time-section">
      <button @click="incrementSeconds" class="time-btn">▲</button>
      <input
        type="number"
        :value="seconds.toString().padStart(2, '0')"
        @input="handleSecondsChange"
        min="0"
        max="59"
        class="time-input"
      />
      <button @click="decrementSeconds" class="time-btn">▼</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TimerInput',
  props: {
    initialMinutes: {
      type: Number,
      default: 0
    },
    initialSeconds: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      minutes: this.initialMinutes,
      seconds: this.initialSeconds
    }
  },
  methods: {
    handleMinutesChange(e) {
      this.minutes = Math.max(0, Math.min(59, Number(e.target.value)));
      this.emitChange();
    },
    handleSecondsChange(e) {
      this.seconds = Math.max(0, Math.min(59, Number(e.target.value)));
      this.emitChange();
    },
    incrementMinutes() {
      this.minutes = Math.min(59, this.minutes + 1);
      this.emitChange();
    },
    decrementMinutes() {
      this.minutes = Math.max(0, this.minutes - 1);
      this.emitChange();
    },
    incrementSeconds() {
      if (this.seconds === 59) {
        this.seconds = 0;
        this.incrementMinutes();
      } else {
        this.seconds++;
      }
      this.emitChange();
    },
    decrementSeconds() {
      if (this.seconds === 0 && this.minutes > 0) {
        this.seconds = 59;
        this.decrementMinutes();
      } else {
        this.seconds = Math.max(0, this.seconds - 1);
      }
      this.emitChange();
    },
    emitChange() {
      this.$emit('change', { minutes: this.minutes, seconds: this.seconds });
    }
  }
}
</script>

<style scoped>
.timer-input {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
}

.time-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.time-input {
  width: 2.5em;
  text-align: center;
  font-size: 1em;
  border: none;
  background: transparent;
  color: #333;
}

.time-btn {
  background: none;
  border: none;
  font-size: 0.8em;
  cursor: pointer;
  color: #007bff;
}

.time-separator {
  margin: 0 0.5em;
}
</style>