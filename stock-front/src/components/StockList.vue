<!-- src/components/StockList.vue -->
<template>
  <div class="stock-list">
    <h3>주식 목록</h3>
    
    <div v-if="stocks.length === 0" class="loading">
      주식 정보를 불러오는 중...
    </div>
    
    <table v-else>
      <thead>
        <tr>
          <th>ID</th>
          <th>이름</th>
          <th>가격</th>
          <th>거래</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="stock in stocks" :key="stock.id">
          <td>{{ stock.id }}</td>
          <td>{{ stock.stockName }}</td>
          <td>{{ stock.stockPrice }}원</td>
          <td>
            <div class="trade-controls">
              <input 
                type="number" 
                v-model="quantities[stock.id]" 
                min="1" 
                placeholder="수량" 
                class="quantity-input"
              >
              <button 
                @click="buyStock(stock.id)" 
                :disabled="!quantities[stock.id] || !currentPlayer"
                class="buy-button"
              >
                구매
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
    
    <div class="add-stock-form">
      <h4>새 주식 추가</h4>
      <div class="form-row">
        <input type="text" v-model="newStock.name" placeholder="주식 이름">
        <input type="number" v-model="newStock.price" placeholder="주식 가격">
        <button @click="addStock" :disabled="!newStock.name || !newStock.price">추가</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import api from '../services/api';

const props = defineProps({
  stocks: {
    type: Array,
    required: true
  },
  currentPlayer: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['buy-stock']);

const quantities = reactive({});
const newStock = reactive({
  name: '',
  price: ''
});

// 주식 구매
const buyStock = (stockId) => {
  const quantity = parseInt(quantities[stockId]);
  if (quantity && quantity > 0) {
    emit('buy-stock', stockId, quantity);
    quantities[stockId] = ''; // 입력 필드 초기화
  }
};

// 새 주식 추가
const addStock = async () => {
  if (!newStock.name || !newStock.price) return;
  
  try {
    await api.addStock(newStock.name, parseInt(newStock.price));
    alert('새 주식이 추가되었습니다.');
    // 입력 필드 초기화
    newStock.name = '';
    newStock.price = '';
    // 주식 목록 새로고침을 위한 이벤트 발생
    // 이 부분은 App.vue에서 처리해야 합니다
  } catch (error) {
    alert('주식 추가에 실패했습니다.');
    console.error(error);
  }
};
</script>

<style scoped>
.stock-list {
  width: 100%;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

th, td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

.loading {
  padding: 20px;
  text-align: center;
  color: #666;
}

.trade-controls {
  display: flex;
  gap: 5px;
}

.quantity-input {
  width: 60px;
  padding: 4px;
}

.buy-button {
  padding: 4px 8px;
}

.add-stock-form {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-row {
  display: flex;
  gap: 10px;
}

input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 8px 12px;
  cursor: pointer;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>