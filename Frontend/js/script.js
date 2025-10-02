// JavaScript para TODO List
// Todas as funções estão comentadas explicando sua finalidade

// Array para armazenar as tarefas em memória
let tasks = [];
let editIndex = null; // Índice da tarefa em edição, se houver

// Função para renderizar as tarefas nas três colunas
function renderTasks() {
  const todoList = document.getElementById('todo-list');
  const doingList = document.getElementById('doing-list');
  const doneList = document.getElementById('done-list');
  todoList.innerHTML = '';
  doingList.innerHTML = '';
  doneList.innerHTML = '';
  tasks.forEach((task, idx) => {
    const li = document.createElement('li');
    li.className = task.status;
    li.innerHTML = `
      <div class="task-info">
        <strong>${task.title}</strong><br>
        <span>${task.description || ''}</span><br>
        <small>Vencimento: ${task.dueDate || '-'}</small><br>
        <div class="status-select">
          <label for="status-select-${idx}" style="font-size:0.9em;">Status:</label>
          <select id="status-select-${idx}" data-idx="${idx}">
            <option value="TODO" ${task.status === 'TODO' ? 'selected' : ''}>A Fazer</option>
            <option value="DOING" ${task.status === 'DOING' ? 'selected' : ''}>Fazendo</option>
            <option value="DONE" ${task.status === 'DONE' ? 'selected' : ''}>Feito</option>
          </select>
        </div>
      </div>
      <div class="task-actions">
        <button onclick="editTask(${idx})" title="Editar"><span class="material-symbols-outlined" style="font-variation-settings: 'wght' 700;">edit</span></button>
        <button onclick="deleteTask(${idx})" class="delete" title="Excluir"><span class="material-symbols-outlined" style="font-variation-settings: 'wght' 700;">delete</span></button>
      </div>
    `;
    // Adiciona listener para mudança de status
    setTimeout(() => {
      const select = document.getElementById(`status-select-${idx}`);
      if (select) {
        select.addEventListener('change', function(e) {
          tasks[idx].status = e.target.value;
          renderTasks();
        });
      }
    }, 0);
    if (task.status === 'TODO') todoList.appendChild(li);
    else if (task.status === 'DOING') doingList.appendChild(li);
    else if (task.status === 'DONE') doneList.appendChild(li);
  });
}

// Função para adicionar ou atualizar uma tarefa
function handleFormSubmit(event) {
  event.preventDefault();
  const title = document.getElementById('title').value.trim();
  const description = document.getElementById('description').value.trim();
  const dueDate = document.getElementById('dueDate').value;
  const status = document.getElementById('status').value;
  if (!title) return;
  const task = { title, description, dueDate, status };
  if (editIndex !== null) {
    // Atualiza tarefa existente
    tasks[editIndex] = task;
    editIndex = null;
    document.getElementById('task-form').querySelector('button[type="submit"]').textContent = 'Adicionar';
  } else {
    // Adiciona nova tarefa
    tasks.push(task);
  }
  document.getElementById('task-form').reset();
  renderTasks();
}

// Função para editar uma tarefa existente
function editTask(idx) {
  const task = tasks[idx];
  document.getElementById('title').value = task.title;
  document.getElementById('description').value = task.description;
  document.getElementById('dueDate').value = task.dueDate;
  document.getElementById('status').value = task.status;
  editIndex = idx;
  document.getElementById('task-form').querySelector('button[type="submit"]').textContent = 'Salvar';
}

// Função para excluir uma tarefa
function deleteTask(idx) {
  if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
    tasks.splice(idx, 1);
    renderTasks();
  }
}

// Inicialização dos listeners
window.onload = function() {
  document.getElementById('task-form').addEventListener('submit', handleFormSubmit);
  renderTasks();
};
