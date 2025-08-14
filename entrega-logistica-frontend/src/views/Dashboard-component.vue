<template>
  <div class="dashboard">
    <h1>Dashboard</h1>
    <p class="subtitle">Bem-vindo ao sistema de agendamentos</p>
    <div class="cards">
      <div class="card">
        <h4>Agendamentos</h4>
        <p>Gerencie as marcações do dia</p>
        <router-link to="/agendamento" class="btn-link">Ir para Agendamentos</router-link>
      </div>
      <div class="card">
        <h4>Tipos Caminhão</h4>
        <p>Cadastre os tipos de caminhão</p>
        <router-link to="/caminhao" class="btn-link">Abrir</router-link>
      </div>
      <div class="card">
        <h4>Paletização</h4>
        <p>Consulte os tipos de paletização</p>
        <router-link to="/paletizacao" class="btn-link">Abrir</router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
export default {}
</script>

<style scoped>
.dashboard h1 {
  margin: 0 0 6px 0;
}
.subtitle {
  color: var(--muted);
  margin: 0 0 16px 0;
}
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 16px;
}
.btn-link {
  display: inline-block;
  margin-top: 10px;
  background: var(--brand-blue);
  color: #fff;
  padding: 8px 12px;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 700;
  transition:
    background var(--transition),
    transform var(--transition);
}
.btn-link:hover {
  background: var(--brand-blue-dark);
  transform: translateY(-1px);
}
</style>
