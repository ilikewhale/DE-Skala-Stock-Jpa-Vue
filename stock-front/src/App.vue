<!-- src/App.vue -->
<template>
  <div class="container">
    <h1>주식 거래 시뮬레이션</h1>
    
    <div class="grid">
      <div class="player-section">
        <h2>플레이어 정보</h2>
        <player-form @player-created="loadCurrentPlayer" />
        
        <div v-if="currentPlayer" class="player-info">
          <h3>{{ currentPlayer.playerId }}님의 정보</h3>
          <p>보유 자금: {{ currentPlayer.playerMoney }}원</p>
          
          <!-- 여기를 PlayerStocks 컴포넌트로 교체 -->
          <player-stocks 
            v-if="playerStocks" 
            :player-id="currentPlayer.playerId" 
            :stocks-data="playerStocks"
            @stocks-updated="loadCurrentPlayer(currentPlayer.playerId)"
          />
          <p v-else>보유 주식 정보를 불러오는 중...</p>
        </div>
      </div>
      
      <div class="stock-section">
        <h2>주식 시장</h2>
        <div class="simulation-controls">
          <button @click="startSimulation" :disabled="isSimulationRunning">시뮬레이션 시작</button>
          <button @click="stopSimulation" :disabled="!isSimulationRunning">시뮬레이션 정지</button>
        </div>
        
        <stock-list 
          :stocks="stocks" 
          :current-player="currentPlayer"
          @buy-stock="buyStock" 
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import api from './services/api';
import PlayerForm from './components/PlayerForm.vue';
import StockList from './components/StockList.vue';
import PlayerStocks from './components/PlayerStocks.vue'; // 추가된 import

// 상태 관리
const stocks = ref([]);
const currentPlayer = ref(null);
const playerStocks = ref('');
const isSimulationRunning = ref(false);
const stockRefreshInterval = ref(null);

// 주식 목록 로드
const loadStocks = async () => {
  try {
    const response = await api.getStocks();
    stocks.value = response.data;
  } catch (error) {
    console.error('주식 정보를 가져오는데 실패했습니다:', error);
  }
};

// 현재 플레이어 정보 로드
const loadCurrentPlayer = async (playerId) => {
  try {
    const response = await api.getPlayerById(playerId);
    currentPlayer.value = response.data;
    loadPlayerStocks(playerId);
  } catch (error) {
    console.error('플레이어 정보를 가져오는데 실패했습니다:', error);
    currentPlayer.value = null;
  }
};

// 플레이어 주식 정보 로드
const loadPlayerStocks = async (playerId) => {
  try {
    const response = await api.getPlayerStocks(playerId);
    playerStocks.value = response.data;
  } catch (error) {
    console.error('플레이어 주식 정보를 가져오는데 실패했습니다:', error);
  }
};

// 주식 구매 처리
const buyStock = async (stockIndex, quantity) => {
  if (!currentPlayer.value) {
    alert('플레이어를 먼저 선택해주세요');
    return;
  }
  
  try {
    await api.buyStock(currentPlayer.value.playerId, stockIndex, quantity);
    loadCurrentPlayer(currentPlayer.value.playerId); // 플레이어 정보 갱신
    loadStocks(); // 주식 정보 갱신
  } catch (error) {
    alert('주식 구매에 실패했습니다.');
    console.error(error);
  }
};

// 시뮬레이션 시작
const startSimulation = async () => {
  try {
    await api.startSimulation();
    isSimulationRunning.value = true;
    
    // 5초마다 주식 정보 갱신
    stockRefreshInterval.value = setInterval(() => {
      loadStocks();
      if (currentPlayer.value) {
        loadCurrentPlayer(currentPlayer.value.playerId);
      }
    }, 5000);
  } catch (error) {
    console.error('시뮬레이션 시작에 실패했습니다:', error);
  }
};

// 시뮬레이션 정지
const stopSimulation = async () => {
  try {
    await api.stopSimulation();
    isSimulationRunning.value = false;
    clearInterval(stockRefreshInterval.value);
  } catch (error) {
    console.error('시뮬레이션 정지에 실패했습니다:', error);
  }
};

// 주식 정보 포맷팅 함수 (더 이상 필요하지 않지만 유지해도 됨)
const formatStocks = (stocksStr) => {
  return stocksStr.replace(/\n/g, '<br>');
};

// 컴포넌트 마운트 시 초기 데이터 로드
onMounted(() => {
  loadStocks();
});

// 컴포넌트 언마운트 시 인터벌 정리
const onUnmounted = () => {
  if (stockRefreshInterval.value) {
    clearInterval(stockRefreshInterval.value);
  }
};
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.player-section, .stock-section {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.simulation-controls {
  margin-bottom: 20px;
}

button {
  padding: 8px 16px;
  margin-right: 10px;
  cursor: pointer;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.player-info {
  margin-top: 20px;
}

.player-stocks {
  margin-top: 15px;
  border-top: 1px solid #eee;
  padding-top: 15px;
}
</style>