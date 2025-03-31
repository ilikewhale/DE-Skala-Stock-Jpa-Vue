<!-- src/components/PlayerForm.vue -->
<template>
    <div class="player-form">
      <h3>플레이어 생성/선택</h3>
      
      <div class="form-group">
        <label for="playerId">플레이어 ID:</label>
        <input type="text" id="playerId" v-model="playerId" placeholder="플레이어 ID를 입력하세요">
      </div>
      
      <div class="form-group" v-if="isCreatingNew">
        <label for="initialMoney">초기 자금:</label>
        <input type="number" id="initialMoney" v-model="initialMoney" placeholder="초기 자금을 입력하세요">
      </div>
      
      <div class="form-actions">
        <button @click="loadPlayer" :disabled="!playerId">플레이어 찾기</button>
        <button @click="createPlayer" :disabled="!playerId || !initialMoney">새 플레이어 생성</button>
      </div>
      
      <div class="toggle-mode">
        <button @click="isCreatingNew = !isCreatingNew">
          {{ isCreatingNew ? '기존 플레이어 선택' : '새 플레이어 생성' }}
        </button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import api from '../services/api';
  
  const playerId = ref('');
  const initialMoney = ref(10000);
  const isCreatingNew = ref(false);
  
  const emit = defineEmits(['player-created']);
  
  // 플레이어 찾기
  const loadPlayer = async () => {
    if (!playerId.value) return;
    
    try {
      const response = await api.getPlayerById(playerId.value);
      if (response.data) {
        emit('player-created', playerId.value);
      } else {
        alert('플레이어를 찾을 수 없습니다.');
      }
    } catch (error) {
      alert('플레이어를 찾을 수 없습니다.');
      console.error(error);
    }
  };
  
  // 새 플레이어 생성
  const createPlayer = async () => {
    if (!playerId.value || !initialMoney.value) return;
    
    try {
      await api.createPlayer(playerId.value, initialMoney.value);
      emit('player-created', playerId.value);
      alert('플레이어가 생성되었습니다!');
    } catch (error) {
      alert('플레이어 생성에 실패했습니다.');
      console.error(error);
    }
  };
  </script>
  
  <style scoped>
  .player-form {
    margin-bottom: 20px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
  }
  
  input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .form-actions {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
  }
  
  button {
    padding: 8px 12px;
    cursor: pointer;
  }
  
  .toggle-mode {
    margin-top: 10px;
  }
  </style>