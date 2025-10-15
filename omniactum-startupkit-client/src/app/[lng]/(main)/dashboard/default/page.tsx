// aj-client/src/app/[lng]/(main)/dashboard/default/page.tsx
"use client";

import { useTranslation } from "@/lib/i18n-client";
import { useParams } from "next/navigation";

export default function Page() {
  const { lng } = useParams() as { lng: string };
  // Not: Henüz çeviri dosyalarını temizlemedik ama bu yapı geleceğe hazır olacak.
  const { t, ready } = useTranslation(lng, 'common');

  if (!ready) {
    // Basit bir yükleme durumu
    return <div>Yükleniyor...</div>;
  }

  return (
    <div className="flex flex-col gap-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Yönetim Paneli</h1>
        <p className="text-muted-foreground">
          Omniactum Startup Kit'e hoş geldiniz! Burası sizin başlangıç paneliniz.
        </p>
      </div>
      <div className="border-2 border-dashed rounded-lg p-12 text-center h-96 flex items-center justify-center">
        <p className="text-muted-foreground">
          Yeni modüllerinizi ve bileşenlerinizi buraya ekleyebilirsiniz.
        </p>
      </div>
    </div>
  );
}