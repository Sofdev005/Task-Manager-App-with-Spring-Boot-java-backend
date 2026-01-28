const API_URL = 'http://localhost:8080/api/v1/tasks';
let allTasks = [];
let isEditMode = false;
let currentEditId = null;

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    checkBackendConnection();
    loadTasks();
    setupEventListeners();
});

// Check backend connection
async function checkBackendConnection() {
    const statusElement = document.getElementById('backendStatus');
    try {
        const response = await fetch(API_URL);
        if (response.ok) {
            statusElement.textContent = 'Connected';
            statusElement.className = 'status-badge status-online';
        } else {
            statusElement.textContent = 'Error';
            statusElement.className = 'status-badge status-offline';
        }
    } catch (error) {
        statusElement.textContent = 'Offline';
        statusElement.className = 'status-badge status-offline';
    }
}

// Setup event listeners
function setupEventListeners() {
    const form = document.getElementById('taskForm');
    const cancelBtn = document.getElementById('cancelBtn');

    form.addEventListener('submit', handleSubmit);
    cancelBtn.addEventListener('click', cancelEdit);
}

// Handle form submission (Create or Update)
async function handleSubmit(e) {
    e.preventDefault();

    const taskId = document.getElementById('taskId').value;
    const title = document.getElementById('title').value.trim();
    const description = document.getElementById('description').value.trim();
    const dueDate = document.getElementById('dueDate').value;
    const priority = document.getElementById('priority').value;
    const status = document.getElementById('status').value;

    if (!title) {
        showToast('Please enter a title', 'error');
        return;
    }

    const taskData = {
        title: title,
        description: description || null,
        dueDate: dueDate || null,
        priority: priority
    };

    // Add status for updates
    if (isEditMode) {
        taskData.status = status;
    }

    try {
        const url = isEditMode ? `${API_URL}/${taskId}` : API_URL;
        const method = isEditMode ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(taskData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            showToast(`Error: ${errorData.error || 'Failed to save task'}`, 'error');
            return;
        }

        const savedTask = await response.json();
        showToast(
            isEditMode ? 'Task updated successfully!' : 'Task created successfully!',
            'success'
        );

        resetForm();
        loadTasks();

    } catch (error) {
        showToast('Failed to connect to server', 'error');
    }
}

// Load all tasks
async function loadTasks() {
    const container = document.getElementById('tasksContainer');
    container.innerHTML = `
        <div class="loading">
            <i class="fas fa-spinner"></i> Loading tasks...
        </div>
    `;

    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        allTasks = await response.json();
        displayTasks(allTasks);

    } catch (error) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle fa-3x"></i>
                <p>Failed to load tasks: ${error.message}</p>
                <button onclick="loadTasks()" class="btn btn-primary" style="margin-top: 15px;">
                    <i class="fas fa-redo"></i> Retry
                </button>
            </div>
        `;
    }
}

// Display tasks
function displayTasks(tasks) {
    const container = document.getElementById('tasksContainer');
    const taskCount = document.getElementById('taskCount');

    taskCount.textContent = tasks.length;

    if (tasks.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-clipboard-list fa-3x"></i>
                <p>No tasks yet. Add your first task!</p>
            </div>
        `;
        return;
    }

    container.innerHTML = '';

    tasks.forEach(task => {
        const taskElement = createTaskElement(task);
        container.appendChild(taskElement);
    });
}

// Create task element
function createTaskElement(task) {
    const div = document.createElement('div');
    div.className = `task-item ${task.status === 'COMPLETE' ? 'complete' : ''}`;
    div.id = `task-${task.id}`;

    const dueDate = task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'No due date';
    const today = new Date();
    const due = task.dueDate ? new Date(task.dueDate) : null;
    const isOverdue = due && due < today && due.toDateString() !== today.toDateString();

    div.innerHTML = `
        <div class="task-header">
            <h3 class="task-title">${escapeHtml(task.title)}</h3>
            <span class="task-priority priority-${task.priority}">${task.priority}</span>
        </div>

        ${task.description ? `<p class="task-description">${escapeHtml(task.description)}</p>` : ''}

        <div class="task-footer">
            <div class="task-meta">
                <span class="task-due ${isOverdue ? 'overdue' : ''}">
                    <i class="fas fa-calendar-alt"></i>
                    ${dueDate} ${isOverdue ? '⚠️' : ''}
                </span>
                <span class="task-status status-${task.status}">
                    <i class="fas fa-${task.status === 'COMPLETE' ? 'check-circle' : 'clock'}"></i>
                    ${task.status}
                </span>
                <span class="task-id">
                    <i class="fas fa-hashtag"></i>
                    ${task.id.substring(0, 8)}...
                </span>
            </div>

            <div class="task-actions">
                <button onclick="editTask('${task.id}')" class="btn btn-sm btn-edit">
                    <i class="fas fa-edit"></i> Edit
                </button>
                <button onclick="toggleTaskStatus('${task.id}')" class="btn btn-sm ${task.status === 'COMPLETE' ? 'btn-warning' : 'btn-success'}">
                    <i class="fas fa-${task.status === 'COMPLETE' ? 'undo' : 'check'}"></i>
                    ${task.status === 'COMPLETE' ? 'Reopen' : 'Complete'}
                </button>
                <button onclick="deleteTask('${task.id}')" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </div>
        </div>
    `;

    return div;
}

// EDIT TASK - Fill form with task data
function editTask(taskId) {
    const task = allTasks.find(t => t.id === taskId);
    if (!task) return;

    // Set edit mode
    isEditMode = true;
    currentEditId = taskId;

    // Update UI
    document.getElementById('formTitle').innerHTML = '<i class="fas fa-edit"></i> Edit Task';
    document.getElementById('submitBtn').innerHTML = '<i class="fas fa-save"></i> Update Task';
    document.getElementById('statusField').style.display = 'block';
    document.getElementById('cancelBtn').style.display = 'inline-flex';
    document.querySelector('.form-section').classList.add('edit-mode');

    // Fill form with task data
    document.getElementById('taskId').value = taskId;
    document.getElementById('title').value = task.title;
    document.getElementById('description').value = task.description || '';
    document.getElementById('dueDate').value = task.dueDate || '';
    document.getElementById('priority').value = task.priority;
    document.getElementById('status').value = task.status;

    // Scroll to form
    document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });

    showToast('Editing task: ' + task.title, 'info');
}

// Cancel edit mode
function cancelEdit() {
    resetForm();
    showToast('Edit cancelled', 'info');
}

// Reset form to create mode
function resetForm() {
    isEditMode = false;
    currentEditId = null;

    // Reset UI
    document.getElementById('formTitle').innerHTML = '<i class="fas fa-plus-circle"></i> Add New Task';
    document.getElementById('submitBtn').innerHTML = '<i class="fas fa-plus"></i> Add Task';
    document.getElementById('statusField').style.display = 'none';
    document.getElementById('cancelBtn').style.display = 'none';
    document.querySelector('.form-section').classList.remove('edit-mode');

    // Clear form
    document.getElementById('taskForm').reset();
    document.getElementById('taskId').value = '';
}

// Toggle task status (quick complete/reopen)
async function toggleTaskStatus(taskId) {
    const task = allTasks.find(t => t.id === taskId);
    if (!task) return;

    const updatedTask = {
        title: task.title,
        description: task.description,
        dueDate: task.dueDate,
        priority: task.priority,
        status: task.status === 'OPEN' ? 'COMPLETE' : 'OPEN'
    };

    try {
        const response = await fetch(`${API_URL}/${taskId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(updatedTask)
        });

        if (!response.ok) {
            const errorData = await response.json();
            showToast(`Update failed: ${errorData.error}`, 'error');
            return;
        }

        await response.json();
        showToast(`Task marked as ${updatedTask.status.toLowerCase()}`, 'success');
        loadTasks();

    } catch (error) {
        showToast('Failed to update task', 'error');
    }
}

// Delete task
async function deleteTask(taskId) {
    if (!confirm('Are you sure you want to delete this task?')) return;

    try {
        const response = await fetch(`${API_URL}/${taskId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Delete failed');

        showToast('Task deleted successfully', 'success');
        loadTasks();

        // If deleting the task we're editing, reset form
        if (currentEditId === taskId) {
            resetForm();
        }

    } catch (error) {
        showToast('Failed to delete task', 'error');
    }
}

// Show toast notification
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;

    setTimeout(() => {
        toast.className = 'toast';
    }, 3000);
}

// Utility: Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Auto-refresh tasks every 30 seconds
setInterval(() => {
    if (document.visibilityState === 'visible') {
        loadTasks();
    }
}, 30000);