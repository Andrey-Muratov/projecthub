

(function () {
    'use strict';

    var STORAGE_KEY = 'projecthub-theme';
    var html = document.documentElement;
    var btn = document.getElementById('themeToggle');
    var icon = document.getElementById('themeToggleIcon');
    if (!btn || !icon) {
        return;
    }

    function syncIcon(theme) {

        if (theme === 'dark') {
            icon.classList.remove('bi-moon-stars');
            icon.classList.add('bi-sun');
        } else {
            icon.classList.remove('bi-sun');
            icon.classList.add('bi-moon-stars');
        }
    }

    syncIcon(html.getAttribute('data-bs-theme') || 'light');

    btn.addEventListener('click', function () {
        var current = html.getAttribute('data-bs-theme') === 'dark' ? 'dark' : 'light';
        var next = current === 'dark' ? 'light' : 'dark';
        html.setAttribute('data-bs-theme', next);
        try {
            localStorage.setItem(STORAGE_KEY, next);
        } catch (e) {  }
        syncIcon(next);
    });
})();
