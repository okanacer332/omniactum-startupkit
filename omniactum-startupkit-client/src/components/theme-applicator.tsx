"use client";

import { useEffect } from 'react';
import { getContrastingTextColor } from '@/lib/utils';

export function ThemeApplicator() {
  useEffect(() => {
    // API_BASE'i doğrudan lib'den almak yerine process.env ile alalım ki client'ta çalışsın.
    const apiBase = process.env.NEXT_PUBLIC_API_BASE ?? "http://localhost:8080";
    
    fetch(`${apiBase}/api/settings/theme`)
      .then(res => res.json())
      .then(colors => {
        if (!colors) return;

        const styleId = 'custom-theme-styles';
        let styleTag = document.getElementById(styleId) as HTMLStyleElement | null;
        if (!styleTag) {
          styleTag = document.createElement('style');
          styleTag.id = styleId;
          document.head.appendChild(styleTag);
        }

        const primaryForeground = getContrastingTextColor(colors.primaryButton);
        const destructiveForeground = getContrastingTextColor(colors.destructiveButton);

        styleTag.innerHTML = `
          :root {
            --background: ${colors.dashboardBackground} !important;
            --sidebar: ${colors.sidebarBackground} !important;
            --primary: ${colors.primaryButton} !important;
            --primary-foreground: ${primaryForeground} !important;
            --destructive: ${colors.destructiveButton} !important;
            --destructive-foreground: ${destructiveForeground} !important;
          }
          .dark {
            /* Koyu tema için de isterseniz ayrı ayarlar ekleyebilirsiniz */
          }
        `;
      })
      .catch(err => {
        console.error("Failed to load custom theme:", err);
      });
  }, []);

  return null; // Bu bileşen görsel bir şey render etmez
}