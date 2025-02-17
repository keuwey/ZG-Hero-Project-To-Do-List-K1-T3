let tasks = []
let currentTaskId = null

document.getElementById("taskForm").addEventListener("submit", e => {
  e.preventDefault()

  const task = {
    id: currentTaskId || Date.now().toString(),
    title: document.getElementById("title").value,
    description: document.getElementById("description").value,
    status: document.getElementById("status").value
  }

  if (currentTaskId) {
    const index = tasks.findIndex(t => t.id === currentTaskId)
    tasks[index] = task
  } else {
    tasks.push(task)
  }

  currentTaskId = null
  renderTasks()
  e.target.reset()
})

document.getElementById("filterStatus").addEventListener("change", renderTasks)

function editTask(id) {
  const task = tasks.find(t => t.id === id)
  document.getElementById("taskId").value = task.id
  document.getElementById("title").value = task.title
  document.getElementById("description").value = task.description
  document.getElementById("status").value = task.status
  currentTaskId = id
}

function deleteTask(id) {
  tasks = tasks.filter(t => t.id !== id)
  renderTasks()
}

function renderTasks() {
  const filter = document.getElementById("filterStatus").value
  const filteredTasks = filter === "ALL" ? tasks : tasks.filter(t => t.status === filter)

  const taskList = document.getElementById("taskList")
  taskList.innerHTML = filteredTasks
    .map(
      task => `
                <tr>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>
                        <span class="status-badge status-${task.status}">
                            ${task.status}
                        </span>
                    </td>
                    <td>
                        <div class="actions">
                            <button class="btn-primary" onclick="editTask('${task.id}')">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn-danger" onclick="deleteTask('${task.id}')">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `
    ).join("")
}

renderTasks()