// src/services/api.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default {
  // 주식 관련 API
  getStocks() {
    return apiClient.get('/stocks');
  },
  getStockById(id) {
    return apiClient.get(`/stocks/${id}`);
  },
  addStock(name, price) {
    return apiClient.post('/stocks', null, { params: { name, price } });
  },
  startSimulation() {
    return apiClient.post('/stocks/simulation/start');
  },
  stopSimulation() {
    return apiClient.post('/stocks/simulation/stop');
  },

  // 플레이어 관련 API
  getPlayers() {
    return apiClient.get('/players');
  },
  getPlayerById(id) {
    return apiClient.get(`/players/${id}`);
  },
  createPlayer(id, money) {
    return apiClient.post('/players', null, { params: { id, money } });
  },
  buyStock(playerId, stockIndex, quantity) {
    return apiClient.post(`/players/${playerId}/buy`, null, { params: { stockIndex, quantity } });
  },
  sellStock(playerId, stockIndex, quantity) {
    return apiClient.post(`/players/${playerId}/sell`, null, { params: { stockIndex, quantity } });
  },
  getPlayerStocks(playerId) {
    return apiClient.get(`/players/${playerId}/stocks`);
  }
};