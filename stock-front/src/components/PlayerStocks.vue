<!-- src/components/PlayerStocks.vue -->
<template>
    <div class="player-stocks-component">
      <h3>보유 주식</h3>
      
      <div v-if="!stocksList.length" class="no-stocks">
        보유한 주식이 없습니다.
      </div>
      
      <table v-else>
        <thead>
          <tr>
            <th>번호</th>
            <th>이름</th>
            <th>가격</th>
            <th>수량</th>
            <th>판매</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(stock, index) in stocksList" :key="index">
            <td>{{ index + 1 }}</td>
            <td>{{ stock.name }}</td>
            <td>{{ stock.price }}원</td>
            <td>{{ stock.quantity }}</td>
            <td>
              <div class="sell-controls">
                <input 
                  type="number" 
                  v-model="sellQuantities[index]" 
                  min="1" 
                  :max="stock.quantity" 
                  placeholder="수량" 
                  class="quantity-input"
                >
                <button 
                  @click="sellStock(index)" 
                  :disabled="!isSellQuantityValid(index, stock.quantity)"
                  class="sell-button"
                >
                  판매
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, computed, watch } from 'vue';
  import api from '../services/api';
  
  const props = defineProps({
    playerId: {
      type: String,
      required: true
    },
    stocksData: {
      type: String,
      required: true
    }
  });
  
  const emit = defineEmits(['stocks-updated']);
  
  const sellQuantities = reactive({});
  const stocksList = ref([]);
  
  // 문자열 형태의 주식 데이터를 파싱하여 배열로 변환
  const parseStocksData = () => {
    if (!props.stocksData || props.stocksData.includes('보유한 주식 목록이 없습니다')) {
      stocksList.value = [];
      return;
    }
    
    const lines = props.stocksData.split('\n').filter(line => line.trim() !== '');
    stocksList.value = lines.map(line => {
      // "1. stockName:stockPrice:stockQuantity" 형식 파싱
      const match = line.match(/\d+\.\s+(.+):(\d+):(\d+)/);
      if (match) {
        return {
          name: match[1],
          price: parseInt(match[2]),
          quantity: parseInt(match[3])
        };
      }
      return null;
    }).filter(stock => stock !== null);
  };
  
  // 판매 수량이 유효한지 확인
  const isSellQuantityValid = (index, maxQuantity) => {
    const quantity = parseInt(sellQuantities[index]);
    return quantity && quantity > 0 && quantity <= maxQuantity;
  };
  
  // 주식 판매
  const sellStock = async (index) => {
    const quantity = parseInt(sellQuantities[index]);
    if (!isSellQuantityValid(index, stocksList.value[index].quantity)) {
      return;
    }
    
    try {
      await api.sellStock(props.playerId, index, quantity);
      emit('stocks-updated');
      sellQuantities[index] = ''; // 입력 필드 초기화
    } catch (error) {
      alert('주식 판매에 실패했습니다.');
      console.error(error);
    }
  };
  
  // stocksData 변경 시 데이터 파싱
  watch(() => props.stocksData, () => {
    parseStocksData();
  }, { immediate: true });
  </script>
  
  <style scoped>
  .player-stocks-component {
    margin-top: 20px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
  }
  
  th, td {
    padding: 8px;
    border: 1px solid #ddd;
    text-align: left;
  }
  
  th {
    background-color: #f2f2f2;
  }
  
  .no-stocks {
    padding: 15px;
    color: #666;
    font-style: italic;
  }
  
  .sell-controls {
    display: flex;
    gap: 5px;
  }
  
  .quantity-input {
    width: 60px;
    padding: 4px;
  }
  
  .sell-button {
    padding: 4px 8px;
  }
  
  button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  </style>