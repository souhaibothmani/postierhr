// JavaScript principal pour le Site RH Postiers

document.addEventListener('DOMContentLoaded', function() {
    // Initialisation des dropdowns Bootstrap
    var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
    var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
        return new bootstrap.Dropdown(dropdownToggleEl);
    });

    // Initialisation des tooltips Bootstrap
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Auto-dismiss des alertes après 5 secondes
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Validation côté client pour les formulaires
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Confirmation pour les actions de suppression
    const deleteButtons = document.querySelectorAll('[data-action="delete"]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            const message = button.getAttribute('data-message') || 'Êtes-vous sûr de vouloir supprimer cet élément ?';
            if (!confirm(message)) {
                event.preventDefault();
            }
        });
    });

    // Gestion des requêtes AJAX avec indication de chargement
    window.showLoading = function(element) {
        element.classList.add('loading');
        const spinner = document.createElement('span');
        spinner.className = 'spinner-border spinner-border-sm me-2';
        spinner.setAttribute('role', 'status');
        element.insertBefore(spinner, element.firstChild);
    };

    window.hideLoading = function(element) {
        element.classList.remove('loading');
        const spinner = element.querySelector('.spinner-border');
        if (spinner) {
            spinner.remove();
        }
    };
});

// Fonctions utilitaires pour les requêtes AJAX
window.postierhrApi = {
    // Méthode générique pour les requêtes AJAX
    request: function(url, options = {}) {
        const defaultOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            credentials: 'same-origin'
        };

        const mergedOptions = Object.assign(defaultOptions, options);

        return fetch(url, mergedOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .catch(error => {
                console.error('Erreur lors de la requête:', error);
                throw error;
            });
    },

    // Méthodes spécialisées
    get: function(url) {
        return this.request(url);
    },

    post: function(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    put: function(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },

    delete: function(url) {
        return this.request(url, {
            method: 'DELETE'
        });
    }
};

// Fonction pour afficher des notifications toast
window.showToast = function(message, type = 'info') {
    const toastContainer = document.querySelector('.toast-container') || createToastContainer();
    
    const toastElement = document.createElement('div');
    toastElement.className = `toast align-items-center text-white bg-${type} border-0`;
    toastElement.setAttribute('role', 'alert');
    toastElement.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    
    toastContainer.appendChild(toastElement);
    
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
    
    toastElement.addEventListener('hidden.bs.toast', function() {
        toastElement.remove();
    });
};

function createToastContainer() {
    const container = document.createElement('div');
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    document.body.appendChild(container);
    return container;
}

// Validation personnalisée pour les mots de passe
window.validatePasswordMatch = function(passwordField, confirmField) {
    confirmField.addEventListener('input', function() {
        if (passwordField.value !== confirmField.value) {
            confirmField.setCustomValidity('Les mots de passe ne correspondent pas');
        } else {
            confirmField.setCustomValidity('');
        }
    });
    
    passwordField.addEventListener('input', function() {
        if (confirmField.value && passwordField.value !== confirmField.value) {
            confirmField.setCustomValidity('Les mots de passe ne correspondent pas');
        } else {
            confirmField.setCustomValidity('');
        }
    });
};

// Formatage des dates en français
window.formatDate = function(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
};

// Calcul automatique des jours de congé
window.calculateDays = function(startDate, endDate) {
    if (!startDate || !endDate) return 0;
    
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end - start);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    
    return diffDays;
};